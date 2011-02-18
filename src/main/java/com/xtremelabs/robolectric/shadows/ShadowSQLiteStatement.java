package com.xtremelabs.robolectric.shadows;

import android.database.sqlite.*;

import java.sql.*;

import com.xtremelabs.robolectric.internal.*;

@Implements(SQLiteStatement.class)
public class ShadowSQLiteStatement {
	public static final String TAG = ShadowSQLiteStatement.class.getSimpleName();
	private String sql;
	
	@Implementation
	public long simpleQueryForLong () {
		Statement stmt;
		try {
			stmt = ShadowSQLiteDatabase.connection.createStatement();
			ResultSet resultSet = stmt.executeQuery(sql);
			
			try {
				if (resultSet.next()) {
					return resultSet.getLong(1);
				} else {
					throw new SQLiteDoneException();
				}
			} finally {
				if (resultSet != null) resultSet.close();
			}
		} catch (SQLException e) {
			System.out.println("SQLException in ShadowSQLiteStatement#simpleQueryForLong" + e);
			throw new RuntimeException(e);
		}
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

}
