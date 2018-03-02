package com.eventsplash.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.eventsplash.login.FingerprintLoginActivity;
import com.eventsplash.providers.databases.FavoritesDatabaseHelper;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;

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

    public static final String _ID = "_id";
    public static final String _USER_NAME = "user_name";
    public static final String _EVENT_NAME = "event_name";

    public static final String SEARCH_BY_USER_NAME_SELECTION = _USER_NAME + "=?";
    public static final String SEARCH_BY_EVENT_NAME_SELECTION = _EVENT_NAME + "=?";
    public static final String SEARCH_BY_USER_NAME_AND_EVENT_NAME_SELECTION =
            SEARCH_BY_USER_NAME_SELECTION +
                    " AND " +
                    SEARCH_BY_EVENT_NAME_SELECTION;

    @Override
    public boolean onCreate() {
        favoritesDatabaseHelper = new FavoritesDatabaseHelper();
        return init();
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        if (!favoritesDatabaseHelper.databaseReady()) {
            if (!init()) {
                navigateToLoginPage();
            }
        }

        switch (sUriMatcher.match(uri)) {
            case 1:
                break;
            default:
        }

        Cursor c = favoritesDatabaseHelper.query(FavoritesDatabaseHelper.FAVORITES_TBNAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder);
        if (c != null) {
            c.setNotificationUri(getContext().getContentResolver(), uri);
        }
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
        if (!favoritesDatabaseHelper.databaseReady()) {
            if (!init()) {
                navigateToLoginPage();
            }
        }

        long rowID = favoritesDatabaseHelper.insert(FavoritesDatabaseHelper.FAVORITES_TBNAME, "", values);

        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }

        throw new SQLException("Failed to add a record into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        if (!favoritesDatabaseHelper.databaseReady()) {
            if (!init()) {
                navigateToLoginPage();
            }
        }

        int count = 0;
        switch (sUriMatcher.match(uri)){
            case 1:
                count = favoritesDatabaseHelper.delete(FavoritesDatabaseHelper.FAVORITES_TBNAME, selection, selectionArgs);
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

    private boolean init() {
        try {
            favoritesDatabaseHelper.init(getContext());
        } catch (InvalidKeyException |
                UnsupportedEncodingException |
                BadPaddingException |
                IllegalBlockSizeException |
                RuntimeException e) {
            return false;
        }
        return true;
    }

    private void navigateToLoginPage() {
        Toast.makeText(getContext(), "Please login to continue", Toast.LENGTH_SHORT).show();
        Intent loginActivity = new Intent(getContext(), FingerprintLoginActivity.class);
        getContext().startActivity(loginActivity);
    }
}
