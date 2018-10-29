package com.arham.docsshaab.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Arham on 28/06/2018.
 */

public class CategoryContract {

    /*
    * To create the base of all URIs which app will use to contact the content provider
    */
    public static final String CONTENT_AUTHORITY = "com.arham.docsshaab";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_CATEGORIES = "health-category-path";

    public static final String PATH_SUB_CATE = "health-sub-category-path";

    public static final String PATH_BOOKMARK = "health-bookmark-path";

    public static class CategoryEntry implements BaseColumns {
        /** Content URI to access the category data in the provider **/
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_CATEGORIES);
        public static final Uri CONTENT_URI_2 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_SUB_CATE);
        public static final Uri CONTENT_URI_3 = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_BOOKMARK);

        /**
         * CONTENT_URI for List of categories
         */
        public static final String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;

        /**
         * CONTENT_URI for a single category
         */
        public static final String CONTENT_ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORIES;

        /** Name of the database table for health categories */
        public static final String TABLE_NAME = "health_category";
        public static final String TABLE_SUBCATE = "health_sub_category";
        public static final String TABLE_BOOKMARK = "docsaab_bookmark";

        /**
         * unique id number for health_category
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * table schema for health_category, health_sub_category and docsaab_bookmark
         */
        public static final String COLUMN_IMAGE_PATH = "image_path";
        public static final String COLUMN_CONTENT_EN = "content_en";
        public static final String COLUMN_CONTENT_HI = "content_hi";
        public static final String COLUMN_CONTENT_BOOK = "content_book";

        /**
         * table schema for health_sub_category
         */
        public final static String SUB_ID = "SUB_ID";

    }

}
