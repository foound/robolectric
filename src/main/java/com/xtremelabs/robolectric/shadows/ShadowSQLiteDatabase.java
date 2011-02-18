package com.xtremelabs.robolectric.shadows;

import static com.xtremelabs.robolectric.Robolectric.*;
import static com.xtremelabs.robolectric.util.SQLite.*;

import android.content.*;
import android.database.*;
import android.database.sqlite.*;

import java.sql.*;
import java.sql.SQLException;
import java.util.*;

import com.xtremelabs.robolectric.internal.*;
import com.xtremelabs.robolectric.util.*;
import com.xtremelabs.robolectric.util.SQLite.SQLStringAndBindings;

/**
 * Shadow for {@code SQLiteDatabase} that simulates the movement of a {@code Cursor} through database tables.
 * Implemented as a wrapper around an embedded SQL database, accessed via JDBC.  The JDBC connection is
 * made available to test cases for use in fixture setup and assertions.
 */
@Implements(SQLiteDatabase.class)
public class ShadowSQLiteDatabase {
    public static Connection connection;

    @Implementation
    public static SQLiteDatabase openDatabase(String path, SQLiteDatabase.CursorFactory factory, int flags) {
        try {
            Class.forName("org.sqlite.JDBC").newInstance();
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            // System.out.println("DB:: getConnection");
        } catch (Exception e) {
            throw new RuntimeException("SQL exception in openDatabase", e);
        }
        
        return newInstanceOf(SQLiteDatabase.class);
    }

    @Implementation
    public long insert(String table, String nullColumnHack, ContentValues values) {
        SQLStringAndBindings sqlInsertString = buildInsertString(table, values);
        try {
        	// System.out.println("DB:: insert: " + sqlInsertString.sql);
            PreparedStatement statement = connection.prepareStatement(sqlInsertString.sql);
            Iterator<Object> columns = sqlInsertString.columnValues.iterator();
            int i = 1;
            while (columns.hasNext()) {
            	Object next = columns.next();
                if (next instanceof byte[]) {
                	statement.setBytes(i++, (byte[]) next);
                } else {
                	statement.setObject(i++, next);
                }
            }

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in insert", e);
        }
        return -1;
    }

	@Implementation
    public Cursor query(boolean distinct, String table, String[] columns,
                        String selection, String[] selectionArgs, String groupBy,
                        String having, String orderBy, String limit) {

        String where = selection;
        if (selection != null && selectionArgs != null) {
            where = buildWhereClause(selection, selectionArgs);
        }

        String sql = SQLiteQueryBuilder.buildQueryString(distinct, table, columns, where, groupBy, having, orderBy, limit);

        ResultSet resultSet;
        try {
        	// System.out.println("DB:: query: " + sql);
            Statement statement = connection.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
            resultSet = statement.executeQuery(sql);
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in query", e);
        }

        SQLiteCursor cursor = new SQLiteCursor(null, null, null, null);
        ((ShadowSQLiteCursor)shadowOf_(cursor)).setResultSet(resultSet);
        return cursor;
    }

    @Implementation
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, null);
    }

    @Implementation
    public Cursor query(String table, String[] columns, String selection,
                        String[] selectionArgs, String groupBy, String having,
                        String orderBy, String limit) {
        return query(false, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
    }

    @Implementation
    public SQLiteStatement compileStatement (String sql) {
    	SQLiteStatement stmt = newInstanceOf(SQLiteStatement.class);
    	ShadowSQLiteStatement shadow = shadowOf_(stmt);
    	shadow.setSql(sql);
    	return stmt;
    }
    
    @Implementation
    public int update(String table, ContentValues values, String whereClause, String[] whereArgs) {
    	SQLStringAndBindings sqlUpdateString = buildUpdateString(table, values, whereClause, whereArgs);

        try {
        	// System.out.println("DB:: update: " + sqlUpdateString.sql);
            PreparedStatement statement = connection.prepareStatement(sqlUpdateString.sql);
            Iterator<Object> columns = sqlUpdateString.columnValues.iterator();
            int i = 1;
            while (columns.hasNext()) {
            	Object next = columns.next();
                if (next instanceof byte[]) {
                	statement.setBytes(i++, (byte[]) next);
                } else {
                	statement.setObject(i++, next);
                }
            }

            return statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in update", e);
        }
    }

    @Implementation
    public int delete(String table, String whereClause, String[] whereArgs) {
    	String sql = buildDeleteString(table, whereClause, whereArgs);

        try {
        	// System.out.println("DB:: delete: " + sql);
            PreparedStatement statement = connection.prepareStatement(sql);
            try {
            	return statement.executeUpdate();
            } finally {
            	statement.close();
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in delete", e);
        }
    }
    
    public static SQLStringAndBindings buildMergeString(String table, ContentValues values) {
        StringBuilder sb = new StringBuilder();

        sb.append("INSERT OR REPLACE INTO ");
        sb.append(table);
        sb.append(" ");

        SQLStringAndBindings columnsValueClause = SQLite.buildColumnValuesClause(values);
        sb.append(columnsValueClause.sql);
        sb.append(";");
        
        return new SQLStringAndBindings(sb.toString(), columnsValueClause.columnValues);
    }

    
    @Implementation
    public long replace (String table, String nullColumnHack, ContentValues values) {
        SQLStringAndBindings sqlInsertString = buildMergeString(table, values);
        try {
        	// System.out.println("DB:: replace: " + sqlInsertString.sql);
            PreparedStatement statement = connection.prepareStatement(sqlInsertString.sql);
            Iterator<Object> columns = sqlInsertString.columnValues.iterator();
            int i = 1;
            while (columns.hasNext()) {
                Object next = columns.next();
                if (next instanceof byte[]) {
                	statement.setBytes(i++, (byte[]) next);
                } else {
                	statement.setObject(i++, next);
                }
                
                // System.out.println("column " + (i-1) + " is " + toString(next));
                
            }

            statement.executeUpdate();
            return -1;
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in replace", e);
        }
    }

    private String toString(Object next) {
		if (next instanceof byte[]) {
			StringBuilder sb = new StringBuilder();
			for (byte b: (byte[])next) {
				sb.append(String.format("%02x ", b));
			}
			return sb.toString();
		}
		return next.toString();
	}

	@Implementation
    public void execSQL(String sql) throws android.database.SQLException {
        if (!isOpen()) {
            throw new IllegalStateException("database not open");
        }

        String scrubbedSQL = sql;

        try {
            connection.createStatement().execute(scrubbedSQL);
        } catch (java.sql.SQLException e) {
            android.database.SQLException ase = new android.database.SQLException();
            ase.initCause(e);
            throw ase;
        }
    }

    @Implementation
    public boolean isOpen() {
        return (connection != null);
    }

    @Implementation
    public void close() {
        if (!isOpen()) {
            return;
        }
        try {
            connection.close();
            connection = null;
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception in close", e);
        }
    }

    /**
     * Allows test cases access to the underlying JDBC connection, for use in
     * setup or assertions.
     *
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }
}
