package com.arham.docsshaab.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Arham on 28/06/2018.
 */

public class CategoryDBHelper extends SQLiteOpenHelper {
    private static final String TAG = CategoryDBHelper.class.getSimpleName();

    private static final String DATEBASE_NAME = "docsaab.db";
    private static final int DATABASE_VERSION = 1;
    Context context;

    public CategoryDBHelper(Context context) {
        super(context, DATEBASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_HEALTH_CATEGORY_TABLE = "CREATE TABLE " + CategoryContract.CategoryEntry.TABLE_NAME + " ( " +
                CategoryContract.CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                CategoryContract.CategoryEntry.COLUMN_CONTENT_EN + " TEXT NOT NULL UNIQUE, " +
                CategoryContract.CategoryEntry.COLUMN_CONTENT_HI + " TEXT NOT NULL UNIQUE );";

        final String SQL_CREATE_HEALTH_SUB_CATE_TABLE = "CREATE TABLE " + CategoryContract.CategoryEntry.TABLE_SUBCATE + " ( " +
                CategoryContract.CategoryEntry._ID + " INTEGER NOT NULL, " +
                CategoryContract.CategoryEntry.SUB_ID + " INTEGER NOT NULL, " +
                CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH + " TEXT NOT NULL, " +
                CategoryContract.CategoryEntry.COLUMN_CONTENT_EN + " TEXT NOT NULL UNIQUE, " +
                CategoryContract.CategoryEntry.COLUMN_CONTENT_HI + " TEXT NOT NULL UNIQUE);";

        final String SQL_CREATE_DOCSAAB_BOOKMARK_TABLE = "CREATE TABLE " + CategoryContract.CategoryEntry.TABLE_BOOKMARK + " ( " +
                CategoryContract.CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CategoryContract.CategoryEntry.COLUMN_CONTENT_BOOK + " TEXT UNIQUE );";

        db.execSQL(SQL_CREATE_HEALTH_CATEGORY_TABLE);
        db.execSQL(SQL_CREATE_HEALTH_SUB_CATE_TABLE);
        db.execSQL(SQL_CREATE_DOCSAAB_BOOKMARK_TABLE);
        Log.d(TAG, "DataBase created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + CategoryContract.CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE " + CategoryContract.CategoryEntry.TABLE_SUBCATE);
        db.execSQL("DROP TABLE " + CategoryContract.CategoryEntry.TABLE_BOOKMARK);
        onCreate(db);
    }


}
