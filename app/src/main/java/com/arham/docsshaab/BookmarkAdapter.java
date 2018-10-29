package com.arham.docsshaab;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v4.app.ShareCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.arham.docsshaab.data.CategoryContract;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import org.w3c.dom.Text;

/**
 * Created by Arham on 04/09/2018.
 */

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.ViewHolder> {

    Cursor mcursor;
    Context mcontext;
    String global_lang = "English";

    ContentResolver bookmarkRes;

    public BookmarkAdapter(Activity context, Cursor cursor) {
        this.mcontext = context;
        this.mcursor = cursor;
        this.bookmarkRes = mcontext.getContentResolver();

        setHasStableIds(true);
    }

    @NonNull
    @Override
    public BookmarkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View cardView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_option_layout, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull final BookmarkAdapter.ViewHolder holder, int position) {
        mcursor.moveToPosition(position);

        final Integer _id_ = mcursor.getInt(mcursor.getColumnIndex(CategoryContract.CategoryEntry._ID));
        String bookmContent = mcursor.getString(mcursor.getColumnIndex(CategoryContract.CategoryEntry.COLUMN_CONTENT_BOOK));


        holder.title.setText(Html.fromHtml(bookmContent));
        holder.bookMarkImg.setImageResource(R.drawable.ic_bookmark_des);

        /*
        Glide.with(mcontext)
                .load(imagePath)
                .apply(new RequestOptions()
                        .placeholder(R.drawable.loading))
                .into(holder.imageView);
        */

        holder.bookMarkImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookmarkRes.delete(CategoryContract.CategoryEntry.CONTENT_URI_3,
                        CategoryContract.CategoryEntry._ID  + " = ?",
                        new String[]{Integer.toString(_id_)});
                Toast.makeText(mcontext, "Item is Removed", Toast.LENGTH_SHORT).show();
                holder.bookMarkImg.setImageResource(R.drawable.ic_bookmark_en);
            }
        });

        holder.shareOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/html");

                shareIntent.putExtra(Intent.EXTRA_TEXT, holder.title.getText().toString());;
                ((Activity)mcontext).startActivity(Intent.createChooser(shareIntent, "Share Tips Using"));
            }
        });

    }

    @Override
    public int getItemCount() {
        if(mcursor != null)
            Log.d("bookmarkcursor count", " " +mcursor.getCount());
        else
            Log.d("bookmarkcursor count", "datacursor is null");
        return (mcursor == null) ? 0 : mcursor.getCount();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        //ImageView imageView;
        ImageView bookMarkImg;
        ImageView shareOption;

        public ViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.titleText);
            //imageView = (ImageView) itemView.findViewById(R.id.icons);
            bookMarkImg = (ImageView) itemView.findViewById(R.id.bookmark);
            shareOption = (ImageView) itemView.findViewById(R.id.share_text);
        }
    }

    public Cursor swapCursor(Cursor cursor) {
        if(mcursor == cursor) {
            return null;
        }
        Cursor oldCursor = mcursor;
        this.mcursor = cursor;
        if(cursor != null) {
            this.notifyDataSetChanged();
        }
        return oldCursor;
    }

    public void setLang(String lang) {
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
