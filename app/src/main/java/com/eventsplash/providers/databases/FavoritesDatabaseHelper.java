package com.eventsplash.providers.databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by jonathanhavstad on 3/1/18.
 */

public class FavoritesDatabaseHelper extends SQLiteOpenHelper {
    public static final String FAVORITES_TBNAME = "favorites";

    private static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FAVORITES_TBNAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " user_name TEXT NOT NULL, " +
                    " event_name TEXT NOT NULL);";

    public FavoritesDatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_DB_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " +  FAVORITES_TBNAME);
        onCreate(db);
    }
}
