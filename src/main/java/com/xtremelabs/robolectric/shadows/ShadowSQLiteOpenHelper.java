package com.xtremelabs.robolectric.shadows;

import android.content.*;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

import com.xtremelabs.robolectric.internal.*;

/**
 * Shadow for {@code SQLiteOpenHelper}.  Provides basic support for retrieving
 * databases and partially implements the subclass contract.  (Currently,
 * support for {@code #onUpgrade} is missing).
 */
@Implements(SQLiteOpenHelper.class)
public class ShadowSQLiteOpenHelper {

    @RealObject private SQLiteOpenHelper realHelper;
    private static SQLiteDatabase database;

    public void __constructor__(Context context, String name, CursorFactory factory, int version) {
        if (database != null) {
            database.close();
        }
        database = null;
    }

    @Implementation
    public synchronized void close() {
        if (database != null) {
            database.close();
        }
        database = null;
    }

    @Implementation
    public synchronized SQLiteDatabase getReadableDatabase() {
        if (database == null) {
            database = SQLiteDatabase.openDatabase("path", null, 0);
            System.out.println("realHelper onCreate");
            realHelper.onCreate(database);
            System.out.println("realHelper onCreate FINISH");
        }

        realHelper.onOpen(database);
        return database;
    }

    @Implementation
    public synchronized SQLiteDatabase getWritableDatabase() {
        if (database == null) {
            database = SQLiteDatabase.openDatabase("path", null, 0);
            realHelper.onCreate(database);
        }

        realHelper.onOpen(database);
        return database;
    }
}
