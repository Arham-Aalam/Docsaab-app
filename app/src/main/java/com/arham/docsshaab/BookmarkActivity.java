package com.arham.docsshaab;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.arham.docsshaab.data.CategoryContract;


public class BookmarkActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    Toolbar ActivityToolbar;
    Spinner ActivitySpinner;
    ArrayAdapter<CharSequence> ActivityAdapter;

    private RecyclerView recyclerView;
    private static final int CATEGORY_LOADER = 0;
    BookmarkAdapter bookmarkAdapter;

    Button emptyBtn;
    ImageView emptyImg;
    TextView emptyTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmark);

        emptyBtn = (Button) findViewById(R.id.empty_button);
        emptyImg = (ImageView) findViewById(R.id.empty_img);
        emptyTxt = (TextView) findViewById(R.id.empty_txt);

        ActivityToolbar =  (Toolbar) findViewById(R.id.toolbar);
        ActivitySpinner = (Spinner) findViewById(R.id.spinner);

        ActivityToolbar.setNavigationIcon(R.drawable.arrow_back);
        ActivityToolbar.setTitle(R.string.app_name);
        ActivityAdapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.drop_menu);
        ActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ActivitySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ActivitySpinner.setAdapter(ActivityAdapter);

        ActivitySpinner.setVisibility(View.GONE);

        listeners();

        recyclerView = (RecyclerView) findViewById(R.id.bookmark_recycler_view);
        recyclerView.setHasFixedSize(true);

        bookmarkAdapter = new BookmarkAdapter(this, null);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(bookmarkAdapter);

        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);

    }

    public void listeners() {
        ActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                bookmarkAdapter.setLang(ActivitySpinner.getSelectedItem().toString());
                getLoaderManager().restartLoader(CATEGORY_LOADER, null, BookmarkActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ActivityToolbar.getChildAt(1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        emptyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(i);
            }
        });
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] data = {
                CategoryContract.CategoryEntry._ID,
                CategoryContract.CategoryEntry.COLUMN_CONTENT_BOOK
        };

        return new CursorLoader(
                this,
                CategoryContract.CategoryEntry.CONTENT_URI_3,
                data,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        bookmarkAdapter.swapCursor(data);

        if(bookmarkAdapter.getItemCount() > 0) {
            emptyTxt.setVisibility(View.GONE);
            emptyBtn.setVisibility(View.GONE);
            emptyImg.setVisibility(View.GONE);
        } else {
            emptyTxt.setVisibility(View.VISIBLE);
            emptyBtn.setVisibility(View.VISIBLE);
            emptyImg.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        bookmarkAdapter.swapCursor(null);
    }

}
