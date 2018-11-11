package com.arham.docsshaab;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.transition.TransitionInflater;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.ShareCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.arham.docsshaab.data.CategoryContract;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.support.v4.content.ContextCompat.startActivity;

/**
 * Created by HP on 17/08/2018.
 */

public class CateListAdapter extends RecyclerView.Adapter<CateListAdapter.ViewHolder> {

    Context mcontext;
    ArrayList<ListData> list_data;
    String global_lang = "English";


    private static final String API_URL = "http://zakarmakar.com/index.php/docsaab/getDocDetail";
    final String IMAGE_URL = "image_url";
    final String RESULTS = "result";
    final String DETAIL_EN = "detail_en";
    final String DETAIL_HI = "detail_hi";
    final String DETAIL_IMAGE = "image";
    final String DETAIL_ID = "doc_detail_id_PK";

    final int id_cat, id_sub_cat;

    private static String image_path;
    private int fetch_Count;
    private ProgressBar pro_bar;

    ContentResolver mcontentResolver;

    public CateListAdapter(Activity context,final int _id_cat,final int _id_sub_cat, ProgressBar p_bar) {

        fetch_Count = 0;
        this.mcontext = context;
        this.id_cat = _id_cat;
        this.id_sub_cat = _id_sub_cat;
        list_data = new ArrayList<>();
        this.pro_bar = p_bar;
        mcontentResolver = context.getContentResolver();

        make_request(10);
    }

    public void make_request(int fetch_c) {
        pro_bar.setVisibility(View.VISIBLE);
        StringRequest jsonListReq = new StringRequest(Request.Method.POST, API_URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    setListData(response);
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
                System.out.println("Request made for id and subid " + id_cat + " " + id_sub_cat);
                params.put("cat_id", String.valueOf(id_cat));
                params.put("sub_cat_id", String.valueOf(id_sub_cat));
                return params;
            }
        };

        RequestQueue mrequest = Volley.newRequestQueue(mcontext);
        mrequest.add(jsonListReq);
        mrequest.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<String>() {

            @Override
            public void onRequestFinished(Request<String> request) {
                pro_bar.setVisibility(View.GONE);
            }
        });
    }

    public void setListData(String response) throws JSONException {

        try {
            JSONObject listJsObj = new JSONObject(response);
            image_path = listJsObj.getString(IMAGE_URL);
            JSONArray listArray = listJsObj.getJSONArray(RESULTS);

            for(int i=0;i<listArray.length();i++) {
                JSONObject resultes = listArray.getJSONObject(i);
                list_data.add(new ListData(resultes.getInt(DETAIL_ID),resultes.getString(DETAIL_EN), resultes.getString(DETAIL_HI), resultes.getString("details_Gujju")));
                /* need to add image name and url
                 * by method set image */
                //System.out.println("jsonresp "+ resultes.getString("details_Gujju"));
            }
        } catch (JSONException e) {
            Log.e("JSON ERR : ", e.getMessage(), e);
            e.printStackTrace();
        }
        notifyChange();
    }


    @Override
    public CateListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_option_layout, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CateListAdapter.ViewHolder holder, int position) {
        final String detail_en = list_data.get(position).desc_en;
        final String detail_hi = list_data.get(position).desc_hi;
        final String detail_gujj = list_data.get(position).desc_gujj;
        int _id = list_data.get(position)._id;
        //String image = list_data.get(position).image_path;

        switch(global_lang) {
            case "Hindi":
                holder.description.setText(Html.fromHtml(detail_hi));
                break;
            case "Gujrati":
                holder.description.setText(Html.fromHtml(detail_gujj));
                break;
            default:
                holder.description.setText(Html.fromHtml(detail_en));
        }
        /*
        *Glide.with(mcontext)
        *        .load(image)
        *        .apply(new RequestOptions()
        *                .placeholder(R.drawable.loading))
        *        .into(holder.imageView);
        */
        // setting example images
        //holder.imageView.setImageResource(R.drawable.loading);
        if(holder.bookmarked == false)
            holder.bookmarkOption.setImageResource(R.drawable.ic_bookmark_en);

        holder.bookmarkOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContentValues contentValues = new ContentValues();
                String bookData = "";

                switch (global_lang) {
                    case "Hindi":
                        bookData = detail_hi;
                        break;
                    case "Gujrati":
                        bookData = detail_gujj;
                        break;
                    default:
                        bookData = detail_en;
                }

                contentValues.put(CategoryContract.CategoryEntry.COLUMN_CONTENT_BOOK, bookData);

                if(holder.bookmarked == false) {
                    /** inserting bookmarked data**/
                    try {
                        mcontentResolver.insert(CategoryContract.CategoryEntry.CONTENT_URI_3, contentValues);
                    } catch (Exception ex) {
                        System.out.println("SQLexception");
                        ex.printStackTrace();
                    }
                    holder.bookmarkOption.setImageResource(R.drawable.ic_bookmark_des);
                    holder.bookmarked = true;
                    Toast.makeText(mcontext, "Item is BookMarked", Toast.LENGTH_SHORT).show();
                }
                else {
                    try {
                        mcontentResolver.delete(CategoryContract.CategoryEntry.CONTENT_URI_3,
                                CategoryContract.CategoryEntry.COLUMN_CONTENT_BOOK  + " = ?",
                                new String[]{bookData});
                    } catch (Exception ex) {
                        System.out.println("SQLexception");
                        ex.printStackTrace();
                    }
                    holder.bookmarkOption.setImageResource(R.drawable.ic_bookmark_en);
                    holder.bookmarked = false;
                    Toast.makeText(mcontext, "Item is Removed", Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                Intent shareIntent = ShareCompat.IntentBuilder.from((Activity) mcontext)
                        .setType("text/html")
                        .setHtmlText(holder.description.getText().toString())
                        .setSubject("Health care Tips")
                        .getIntent();
                if(shareIntent.resolveActivity(((Activity)mcontext).getPackageManager()) != null) {
                    ((Activity)mcontext).startActivity(Intent.createChooser(shareIntent, "Share Tips Using"));
                }
                */
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");

                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.description.getText().toString());
                ((Activity)mcontext).startActivity(Intent.createChooser(shareIntent, "Share Tips Using"));
            }
        });
    }

    @Override
    public int getItemCount() {
        if(list_data == null)
            return 0;
        return list_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //ImageView transitonImg;
        public TextView description;
        //ImageView imageView;
        public ImageView bookmarkOption;
        public boolean bookmarked = false;
        public ImageView shareOption;

        public ViewHolder(final View itemView) {
            super(itemView);
            description = (TextView) itemView.findViewById(R.id.titleText);
            //imageView = (ImageView) itemView.findViewById(R.id.icons);
            bookmarkOption = (ImageView) itemView.findViewById(R.id.bookmark);
            shareOption = (ImageView) itemView.findViewById(R.id.share_text);
           //transitonImg = (ImageView) itemView.findViewById(R.id.icons);

            /*
            // code for Starting detail activity

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        if(pos >= 0 && pos < getItemCount()) {
                            System.out.println("position = " + pos);

                            // need to add url
                            //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            //transitonImg.setTransitionName(list_data.get(pos).image_path);
                            //  }

                            //remove this if above comment is removed
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                transitonImg.setTransitionName("https://www.free-pictures-photos.com/landscapes/photo-moraine-u7zhh.jpg");
                            }

                            Intent intent = new Intent( mcontext, DocDetailActivity.class);
                            if(global_lang.equals("English")) {
                                intent.putExtra("DETAIL_TEXT", list_data.get(pos).desc_en);
                                intent.putExtra("TITLE", list_data.get(pos).desc_en.split("\n", 1)[0]);
                                intent.putExtra("DETAIL_IMAGE", ViewCompat.getTransitionName(transitonImg));
                            }
                            else {
                                intent.putExtra("DETAIL_TEXT", list_data.get(pos).desc_hi);
                                intent.putExtra("TITLE", list_data.get(pos).desc_hi.split("\n", 1)[0]);
                                //intent.putExtra("DETAIL_IMAGE", list_data.get(pos).image_path);

                                //remove this if above code is uncommented.
                                intent.putExtra("DETAIL_IMAGE", "https://www.free-pictures-photos.com/landscapes/photo-moraine-u7zhh.jpg");
                            }

                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                    (Activity) mcontext,
                                    transitonImg,
                                    ViewCompat.getTransitionName(transitonImg));

                            mcontext.startActivity(intent, options.toBundle());

                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            });
            */
        }

    }

    public void notifyChange() {
        notifyDataSetChanged();
    }

    public void setLang(String l) {
        this.global_lang = l;
        notifyChange();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public ArrayList<ListData> getData() {
        return this.list_data;
    }
}
