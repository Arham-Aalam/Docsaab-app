package com.arham.docsshaab;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.arham.docsshaab.data.CategoryContract;

import java.io.File;

public class SubCateActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

    Toolbar ActivityToolbar;
    Spinner ActivitySpinner;
    ArrayAdapter<CharSequence> ActivityAdapter;
    //TextView titleText;

    private RecyclerView recyclerView;
    SubCategoryAdapter subCategoryAdapter;
    private static final int CATEGORY_LOADER = 1;

    int iId;
    String _ID = "_ID";
    String LANG_NAME = "LANG_NAME";
    String titleName;

    DrawerLayout drawer;
    NavigationView navigationView;
    View toolBarLogoView, contentView;

    private static final float END_SCALE = 0.7f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityToolbar =  (Toolbar) findViewById(R.id.toolbar);
        ActivitySpinner = (Spinner) findViewById(R.id.spinner);
        //titleText = (TextView) findViewById(R.id.headingTxt);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView)findViewById(R.id.nav_view);
        contentView = findViewById(R.id.main_content);

        ActivityToolbar.setNavigationIcon(R.drawable.menu);
        ActivityToolbar.setTitle(R.string.app_name);
        ActivityAdapter = ArrayAdapter.createFromResource(this, R.array.languagesForCategories, R.layout.drop_menu);
        ActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ActivitySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ActivitySpinner.setAdapter(ActivityAdapter);
        navigationView.setNavigationItemSelectedListener(this);
        toolBarLogoView = ActivityToolbar.getChildAt(1);
        //getWindow().getAttributes().windowAnimations = R.style.MyCustomTheme;
        overridePendingTransition(R.anim.slide_in_bottum,R.anim.slide_out_bottom);

        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra(_ID)) {
            this.iId = getIntent().getExtras().getInt(_ID);
            this.titleName = getIntent().getExtras().getString(LANG_NAME);
            if(!(getIntent().getExtras().getString("choosed_lang").matches(ActivitySpinner.getSelectedItem().toString()))) {
                int pos = ActivitySpinner.getSelectedItemPosition();
                ActivitySpinner.setSelection(++pos%2);
            }

           // titleText.setText(titleName);
        }
        //remove this if spinner is needed
        ActivitySpinner.setVisibility(View.GONE);

        listeners();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        subCategoryAdapter = new SubCategoryAdapter(this, null);
        subCategoryAdapter.setCateId(this.iId);
        recyclerView.setAdapter(subCategoryAdapter);

        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }

    public void listeners() {
        ActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subCategoryAdapter.changeLang(ActivitySpinner.getSelectedItem().toString());
                getLoaderManager().restartLoader(CATEGORY_LOADER, null, SubCateActivity.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        navigationView.setBackgroundColor(Color.TRANSPARENT);
        drawer.setScrimColor(Color.TRANSPARENT);

        toolBarLogoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(Gravity.LEFT);
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                final float diffScaledOffset = slideOffset * (1 - END_SCALE);
                final float offsetScale = 1 - diffScaledOffset;
                contentView.setScaleX(offsetScale);
                contentView.setScaleY(offsetScale);

                // Translate the View, accounting for the scaled width
                final float xOffset = drawerView.getWidth() * slideOffset;
                final float xOffsetDiff = contentView.getWidth() * diffScaledOffset / 2;
                final float xTranslation = xOffset - xOffsetDiff;
                contentView.setTranslationX(xTranslation);
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                ActivityToolbar.setNavigationIcon(R.drawable.cross_menu);
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                ActivityToolbar.setNavigationIcon(R.drawable.menu);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    // todo
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] data = {
                CategoryContract.CategoryEntry.SUB_ID,
                CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH,
                CategoryContract.CategoryEntry.COLUMN_CONTENT_EN,
                CategoryContract.CategoryEntry.COLUMN_CONTENT_HI
        };

        return new CursorLoader(
                this,
                CategoryContract.CategoryEntry.CONTENT_URI_2,
                data,
                "_ID=" ,
                new String[]{Integer.toString(iId)},
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        subCategoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        subCategoryAdapter.swapCursor(null);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                drawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(getApplicationContext(),MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_Share:
                drawer.closeDrawer(Gravity.LEFT);
                ApplicationInfo applicationInfo = getApplicationContext().getApplicationInfo();
                String apkPath = applicationInfo.sourceDir;

                Intent intent  = new Intent(Intent.ACTION_SEND);
                intent.setType("applications/und.android.package.archive");

                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(new File(apkPath)));
                startActivity(Intent.createChooser(intent, getResources().getString(R.string.shareTitle)));
                break;
            case R.id.bookmarks:
                drawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(getApplicationContext(),BookmarkActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
            case R.id.nav_about_us:
                drawer.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(getApplicationContext(), AboutUsPageActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
                break;
        }
        return true;
    }
}
