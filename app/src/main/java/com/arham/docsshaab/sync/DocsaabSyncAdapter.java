package com.arham.docsshaab.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.AuthenticationRequiredException;
import android.app.DownloadManager;
import android.app.VoiceInteractor;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SyncRequest;
import android.content.SyncResult;
import android.net.ParseException;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arham.docsshaab.data.CategoryContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.sql.SQLOutput;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Arham on 28/06/2018.
 */

public class DocsaabSyncAdapter extends AbstractThreadedSyncAdapter {
    public final String LOG_TAG = DocsaabSyncAdapter.class.getSimpleName();

    // here 60 (seconds) * 180 = 3 hours
    public static final int SYNC_INTERVAL = 60 * 180;
    public static final int SYNC_FLEXTIME = SYNC_INTERVAL/3;
    ContentResolver mContentResolver;

    public DocsaabSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);

        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        mContentResolver.delete(CategoryContract.CategoryEntry.CONTENT_URI, null, null);
        mContentResolver.delete(CategoryContract.CategoryEntry.CONTENT_URI_2, null, null);
        Log.d(LOG_TAG, "Starting sync");
        BufferedReader reader = null;

        HttpURLConnection urlConnection = null;

        // for raw JSON data
        String cate_JsonStr = null;
        int ids = 0;

        try {

            /**  Fetching Data for Main Categories */
            final String BASE_URL = "http://www.zakarmakar.com/index.php/docsaab/getDocCategory";

            Uri buildUri = Uri.parse(BASE_URL).buildUpon()
                    .build();

            URL url = new URL(buildUri.toString());
            Log.d(LOG_TAG, "The URL link is "+ url);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(1000000);
            urlConnection.setConnectTimeout(1500000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // read input stream
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if(inputStream != null) {
                reader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() != 0) {
                    cate_JsonStr = buffer.toString();
                    ids = getDocsData(cate_JsonStr);
                }

            }

            /**  Fetching Data for SUB Categories */
            System.out.println("Starting subcategory sync");
            for(int idItr=1; idItr <= ids; idItr++) {
                Log.d("Id no : ", String.valueOf(idItr));
                makeSubRequest(idItr);
            }


        } catch (ParseException e) {
            Log.e("SyncException", "ParseException");
            syncResult.stats.numParseExceptions++;
        } catch (IOException e) {
            Log.e(LOG_TAG, "ERROR passing data");
            syncResult.stats.numParseExceptions++;
            syncResult.delayUntil = 180;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            syncResult.stats.numParseExceptions++;
            syncResult.delayUntil = 180;
            //e.printStackTrace();
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
            if(reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream");
                }
            }
        }
        return;
    }

    private void getSubData(String jsonStr, int id) throws JSONException {
        System.out.println( "Sub response" +jsonStr);

        JSONObject subObj = new JSONObject(jsonStr);
        String imageUrlPath;
        ContentValues subCateValues = null;
        try {
            if(!subObj.getBoolean("status")){
                return;
            }
            imageUrlPath = subObj.getString("image_url");
            JSONArray subArr = subObj.getJSONArray("result");
            String en_name = null, hi_name = null, imageName = null;
            JSONObject innerObj = null;

            for(int itr = 0; itr < subArr.length(); itr++) {
                innerObj = subArr.getJSONObject(itr);

                en_name = innerObj.getString("sub_cname_en");
                hi_name = innerObj.getString("sub_cname_hi");
                imageName = innerObj.getString("image");


                subCateValues = new ContentValues();
                subCateValues.put(CategoryContract.CategoryEntry._ID, id);
                subCateValues.put(CategoryContract.CategoryEntry.SUB_ID, itr + 1);
                subCateValues.put(CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH, imageUrlPath + imageName);
                subCateValues.put(CategoryContract.CategoryEntry.COLUMN_CONTENT_EN, en_name);
                subCateValues.put(CategoryContract.CategoryEntry.COLUMN_CONTENT_HI, hi_name);

                mContentResolver.insert(CategoryContract.CategoryEntry.CONTENT_URI_2, subCateValues);
            }
            Log.d(LOG_TAG, "Sub Sync complete " + subArr.length() + " Records Inserted");

        }  catch (JSONException ex) {
            Log.e("JsonException: ", ex.getMessage());
        }
    }

    private void makeSubRequest(final int id) {
        final String baseSubURL = "http://zakarmakar.com/index.php/docsaab/getDocsubCategory";
        StringRequest jsonSubReq = new StringRequest(Request.Method.POST, baseSubURL, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    getSubData(response, id);
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("err :" + error.getMessage());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cat_id", String.valueOf(id));
                return params;
            }
        };
        Volley.newRequestQueue(getContext())
                .add(jsonSubReq);
    }

    private int getDocsData(String jsonStr) throws JSONException {
        final String IMAGE_URL = "image_url";
        final String CAT_RESULTS = "result";

        final String NAME_EN = "name_en";
        final String NAME_HI = "name_hi";
        final String IMAGE_NAME = "image";
        String imagePath = null;

        try {

            JSONObject cateObject = new JSONObject(jsonStr);
            imagePath = cateObject.getString(IMAGE_URL);
            JSONArray cateArray = cateObject.getJSONArray(CAT_RESULTS);

            Log.d("Image Path", imagePath);
            int i = 0;

            for(i=0; i < cateArray.length(); i++) {

                String en_name, hi_name, imageName;

                JSONObject resultes = cateArray.getJSONObject(i);

                en_name = resultes.getString(NAME_EN);
                hi_name = resultes.getString(NAME_HI);
                imageName = resultes.getString(IMAGE_NAME);

                ContentValues cateValues = new ContentValues();

                cateValues.put(CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH, imagePath + imageName);
                cateValues.put(CategoryContract.CategoryEntry.COLUMN_CONTENT_EN, en_name);
                cateValues.put(CategoryContract.CategoryEntry.COLUMN_CONTENT_HI, hi_name);

                mContentResolver.insert(CategoryContract.CategoryEntry.CONTENT_URI, cateValues);
            }
            Log.d(LOG_TAG, "Sync complete " + cateArray.length() + " Records Inserted");
            return i;
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

        return 0;
    }

    public static void configurePeriodicSync(Context context, int syncInterval, int flexTime) {
        Account account = getSyncAccount(context);
        String authority = "com.arham.docsshaab";
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            SyncRequest request = new SyncRequest.Builder().
                    syncPeriodic( syncInterval, flexTime).
                    setSyncAdapter(account, authority).
                    setExtras(new Bundle()).build();
            ContentResolver.requestSync(request);
        } else {
            //ContentResolver.addPeriodicSync(account, authority, new Bundle(), syncInterval);
            ContentResolver.setSyncAutomatically(account, authority, true);
        }
    }

    public static void syncImmediately(Context context) {
        Bundle bundle = new Bundle();
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        bundle.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        ContentResolver.requestSync(getSyncAccount(context), "com.arham.docsshaab", bundle);
    }

    public static Account getSyncAccount(Context context) {
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);

        Account newAccount = new Account("DocSaab", "docsshaab.arham.com");

        if(null == accountManager.getPassword(newAccount)) {

            if(!accountManager.addAccountExplicitly(newAccount, "", null)) {
                return null;
            }

            onAccountCreated(newAccount, context);
        }
        return newAccount;
    }

    private static void onAccountCreated(Account account, Context context) {
        DocsaabSyncAdapter.configurePeriodicSync(context, SYNC_INTERVAL, SYNC_FLEXTIME);

        ContentResolver.setSyncAutomatically(account, "com.arham.docsshaab", true);

        syncImmediately(context);
    }

    public static void initializeSyncAdapter(Context context) {
        getSyncAccount(context);
    }
}
