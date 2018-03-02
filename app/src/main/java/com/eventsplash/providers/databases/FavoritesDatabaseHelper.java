package com.eventsplash.providers.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.hardware.fingerprint.FingerprintManager;
import android.util.Base64;

import com.eventsplash.login.components.DaggerFingerprintLoginComponent;
import com.eventsplash.login.components.FingerprintLoginComponent;
import com.eventsplash.login.modules.FingerprintLoginModule;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

/**
 * Created by jonathanhavstad on 3/1/18.
 */

public class FavoritesDatabaseHelper {
    public static final String FAVORITES_TBNAME = "favorites";

    private static final String CREATE_DB_TABLE =
            " CREATE TABLE " + FAVORITES_TBNAME +
                    " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    " user_name TEXT NOT NULL, " +
                    " event_name TEXT NOT NULL);";

    private static final String DBNAME = "user_db";
    private static final String DB_EXT = ".db";

    private SQLiteDatabase database;

    private FingerprintLoginComponent fingerprintLoginComponent;
    private FingerprintManager.CryptoObject cryptoObject;

    public void init(Context context) throws InvalidKeyException, UnsupportedEncodingException, BadPaddingException, IllegalBlockSizeException {
        if (initCrypto()) {
            Cipher cipher = cryptoObject.getCipher();
            byte[] plaintTextByteArray = DBNAME.getBytes(StandardCharsets.UTF_8);
            byte[] cipherText = cipher.doFinal(plaintTextByteArray);
            SQLiteDatabase.loadLibs(context);
            File databaseFile = context.getDatabasePath(DBNAME + DB_EXT);
            databaseFile.mkdirs();
            databaseFile.delete();
            database = SQLiteDatabase.openOrCreateDatabase(databaseFile,
                    Base64.encodeToString(cipherText, Base64.DEFAULT),
                    null);
            database.execSQL(CREATE_DB_TABLE);
        }
    }

    private static String padString(String source) {
        char paddingChar = 0;
        int size = 8;
        int x = source.length() % size;
        int padLength = size - x;
        StringBuilder paddedSb = new StringBuilder(source);
        for (int i = 0; i < padLength; i++) {
            paddedSb.append(paddingChar);
        }
        return paddedSb.toString();
    }

    public boolean databaseReady() {
        return database != null;
    }

    public Cursor query(String table,
                        String[] columns,
                        String selection,
                        String[] selectionArgs,
                        String groupBy,
                        String having,
                        String orderBy) {
        if (database != null) {
            return database.query(table, columns, selection, selectionArgs, groupBy, having, orderBy);
        }
        return null;
    }

    public long insert(String table, String nullColumnHack, ContentValues contentValues) {
        if (database != null) {
            return database.insert(table, nullColumnHack, contentValues);
        }
        return -1L;
    }

    public int delete(String table, String whereClause, String[] whereArgs) {
        if (database != null) {
            return database.delete(table, whereClause, whereArgs);
        }
        return 0;
    }

    private boolean initCrypto() {
        fingerprintLoginComponent = DaggerFingerprintLoginComponent.builder()
                .fingerprintLoginModule(new FingerprintLoginModule("AndroidKeyStore",
                        true))
                .build();

        cryptoObject =
                new FingerprintManager.CryptoObject(fingerprintLoginComponent.providesCipher());

        return true;
    }
}
