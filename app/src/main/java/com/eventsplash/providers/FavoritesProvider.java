package com.eventsplash.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.eventsplash.providers.databases.FavoritesDatabaseHelper;

/**
 * Created by jonathanhavstad on 3/1/18.
 */

public class FavoritesProvider extends ContentProvider {
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    public static final String PROVIDER_NAME = "com.eventsplash.providers";
    public static final String FAVORITES_PATH = "favorites";
    public static final String URL = "content://" + PROVIDER_NAME + "/" + FAVORITES_PATH;
    public static final Uri CONTENT_URI = Uri.parse(URL);

    static {
        sUriMatcher.addURI(PROVIDER_NAME, FAVORITES_PATH, 1);
    }

    private FavoritesDatabaseHelper favoritesDatabaseHelper;

    private static final String DBNAME = "user_db";

    public static final String _ID = "_id";
    public static final String _USER_NAME = "user_name";
    public static final String _EVENT_NAME = "event_name";

    public static final String SEARCH_BY_USER_NAME_SELECTION = _USER_NAME + "=?";
    public static final String SEARCH_BY_EVENT_NAME_SELECTION = _EVENT_NAME + "=?";
    public static final String SEARCH_BY_USER_NAME_AND_EVENT_NAME_SELECTION =
            SEARCH_BY_USER_NAME_SELECTION +
                    " AND " +
                    SEARCH_BY_EVENT_NAME_SELECTION;

    private SQLiteDatabase db;

    @Override
    public boolean onCreate() {
        favoritesDatabaseHelper = new FavoritesDatabaseHelper(
            getContext(),
            DBNAME,
            null,
            1
        );
        db = favoritesDatabaseHelper.getWritableDatabase();
        return db != null;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        qb.setTables(FavoritesDatabaseHelper.FAVORITES_TBNAME);

        switch (sUriMatcher.match(uri)) {
            case 1:
                break;
            default:
        }

        Cursor c = qb.query(db,	projection,	selection,
                selectionArgs,null, null, sortOrder);
        c.setNotificationUri(getContext().getContentResolver(), uri);
        return c;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return "vnd.android.cursor.item/vnd.eventsplash.user_favorite_event";
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long rowID = db.insert(FavoritesDatabaseHelper.FAVORITES_TBNAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int count = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = db.delete(FavoritesDatabaseHelper.FAVORITES_TBNAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
