package com.arham.docsshaab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.arham.docsshaab.data.CategoryContract;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

/**
 * Created by HP on 29/06/2018.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    Cursor dataCursor;
    Context context;
    String global_lang = "English";

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        ImageView imageView;
        ProgressBar pb;

        public ViewHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.titleText);
            imageView = (ImageView) itemView.findViewById(R.id.icons);
            pb = (ProgressBar) itemView.findViewById(R.id.img_progress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if(pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, SubCateActivity.class);
                        if(pos >= 0 && pos < getItemCount()) {
                            dataCursor.moveToPosition(pos);
                            intent.putExtra("_ID", dataCursor.getInt(dataCursor.getColumnIndex(CategoryContract.CategoryEntry._ID)));
                            intent.putExtra("choosed_lang", global_lang);
                            if(global_lang.equals("English")) {
                                intent.putExtra("LANG_NAME", dataCursor.getString(dataCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CONTENT_EN)));
                            } else {
                                intent.putExtra("LANG_NAME", dataCursor.getString(dataCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CONTENT_HI)));
                            }
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);
                        } else {
                            throw new IllegalArgumentException();
                        }
                    }
                }
            });
        }
    }

    public CategoryAdapter(Activity mContext, Cursor cursor) {
        dataCursor = cursor;
        context = mContext;
        setHasStableIds(true);
    }

    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View cardview = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.option_layout, parent, false);
        return new ViewHolder(cardview);
    }

    @Override
    public void onBindViewHolder(final CategoryAdapter.ViewHolder holder, int position) {

        dataCursor.moveToPosition(position);

        String imagePath = dataCursor.getString(dataCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH));
        String cEn_name = dataCursor.getString(dataCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CONTENT_EN));
        String cHi_name = dataCursor.getString(dataCursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CONTENT_HI));

        if(global_lang.equals("English"))
            holder.name.setText(cEn_name);
        else
            holder.name.setText(cHi_name);


        Glide.with(context)
                .load(imagePath)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {

                        holder.pb.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.imageView);
    }


    public Cursor swapCursor(Cursor cursor) {
        if(dataCursor == cursor) {
            return null;
        }
        Cursor oldCursor = dataCursor;
        this.dataCursor = cursor;
        if(cursor != null) {
            this.notifyDataSetChanged();
        }
        Cursor democursor = cursor;
        if(democursor.moveToFirst()) {
            do {
                int tt = democursor.getInt(0);
                System.out.println("Cursor_Primary " + tt);
            } while (democursor.moveToNext());
        }
        return oldCursor;
    }

    @Override
    public int getItemCount() {
        if(dataCursor != null)
            Log.d("datacursor count", " " +dataCursor.getCount());
        else
            Log.d("datacursor count", "datacursor is null");

        return (dataCursor == null) ? 0 : dataCursor.getCount();
    }

    public void changeLang(String lang) {
        this.global_lang = lang;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void notifyAdapter() {
        notifyDataSetChanged();
    }
}
