package com.arham.docsshaab.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.sql.SQLException;

/**
 * Created by Arham on 28/06/2018.
 */

public class CategoryProvider extends ContentProvider {
    public static final String LOG_TAG = CategoryProvider.class.getSimpleName();

    private static final int HEALTH_CATS = 100;

    private static final int HEALTH_CAT_ID = 101;

    private static final int HEALTH_SUB_CATS = 102;

    private static final int HEALTH_SUB_CATS_ID = 103;

    private static final int HEALTH_BOOKMARK = 104;

    private static final int HEALTH_BOOKMARK_ID = 105;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_CATEGORIES, HEALTH_CATS);

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_CATEGORIES + "/#", HEALTH_CAT_ID);

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_SUB_CATE, HEALTH_SUB_CATS);

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_SUB_CATE + "/#", HEALTH_SUB_CATS_ID);

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_BOOKMARK , HEALTH_BOOKMARK);

        sUriMatcher.addURI(CategoryContract.CONTENT_AUTHORITY, CategoryContract.PATH_BOOKMARK + "/#", HEALTH_BOOKMARK_ID);
    }

    private CategoryDBHelper cDBHelper;

    @Override
    public boolean onCreate() {
        cDBHelper = new CategoryDBHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
            switch (sUriMatcher.match(uri)) {
                case HEALTH_CATS:
                    return cDBHelper.getReadableDatabase().rawQuery("SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_NAME + ";", null);
                case HEALTH_SUB_CATS:
                    return cDBHelper.getReadableDatabase().rawQuery("SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_SUBCATE + " WHERE " + selection + "'" + selectionArgs[0] + "';", null);
                case HEALTH_BOOKMARK:
                    return cDBHelper.getReadableDatabase().rawQuery("SELECT * FROM " + CategoryContract.CategoryEntry.TABLE_BOOKMARK + ";", null);
            }
        return null;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case HEALTH_CATS:
                return insertCategory(uri, values, CategoryContract.CategoryEntry.TABLE_NAME);
            case HEALTH_SUB_CATS:
                return insertCategory(uri, values, CategoryContract.CategoryEntry.TABLE_SUBCATE);
            case HEALTH_BOOKMARK:
                return insertCategory(uri, values, CategoryContract.CategoryEntry.TABLE_BOOKMARK);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertCategory(Uri uri, ContentValues values, final String TABLE_NAME) {
        SQLiteDatabase database = cDBHelper.getWritableDatabase();
        long id = -1;

        try {
            // insert the new Table
            id = database.insertOrThrow(TABLE_NAME, null, values);
        } catch (SQLiteConstraintException ex) {
            Log.e("SQL Constraints :", "value should be unique\n\n");
            return null;
        }
        if(id == -1) {
            Log.d(LOG_TAG, "Failed to insert row for "+ uri);
            return null;
        }
        // notify all listeners that the data has changed for the category
        getContext().getContentResolver().notifyChange(uri, null);

        // return the uri with the ID
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (sUriMatcher.match(uri)) {
            case HEALTH_CATS:
                return deleteRow(uri, selection, selectionArgs, CategoryContract.CategoryEntry.TABLE_NAME);
            case HEALTH_SUB_CATS:
                return deleteRow(CategoryContract.CategoryEntry.CONTENT_URI_2,
                        CategoryContract.CategoryEntry._ID  + " = ?",
                        new String[]{selection}, CategoryContract.CategoryEntry.TABLE_SUBCATE);
            case HEALTH_BOOKMARK:
                return deleteRow(uri, selection, selectionArgs, CategoryContract.CategoryEntry.TABLE_BOOKMARK);
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    private int deleteRow(Uri uri, String selection, String[] selectionArgs, final String TABLE_NAME) {
        SQLiteDatabase database = cDBHelper.getWritableDatabase();
        int id = -1, cnt;

        try {
            id = database.delete(TABLE_NAME, selection, selectionArgs);
        } catch (Exception ex) {
            Log.e("SQLException", ex.getMessage());
            ex.printStackTrace();
        }
        if(id == -1) {
            Log.d(LOG_TAG, "Failed to delete a row for "+ uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri, null);

        return id;
    }

}
