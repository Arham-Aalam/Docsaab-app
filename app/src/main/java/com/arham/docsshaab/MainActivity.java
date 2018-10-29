package com.arham.docsshaab;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.arham.docsshaab.data.CategoryContract;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.io.File;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>, NavigationView.OnNavigationItemSelectedListener {

    Toolbar ActivityToolbar;
    Spinner ActivitySpinner;
    ArrayAdapter<CharSequence> ActivityAdapter;

    private RecyclerView recyclerView;
    CategoryAdapter categoryAdapter;
    private static final int CATEGORY_LOADER = 0;

    DrawerLayout drawer;
    NavigationView navigationView;
    View toolBarLogoView, contentView;

    private static final float END_SCALE = 0.7f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTimeUser();
        ActivityToolbar =  (Toolbar) findViewById(R.id.toolbar);
        ActivitySpinner = (Spinner) findViewById(R.id.spinner);
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


        listeners();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        if(this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }

        categoryAdapter = new CategoryAdapter(this, null);
        recyclerView.setAdapter(categoryAdapter);

        getLoaderManager().initLoader(CATEGORY_LOADER, null, this);
    }

    public void listeners() {
        ActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryAdapter.changeLang(ActivitySpinner.getSelectedItem().toString());
                    getLoaderManager().restartLoader(CATEGORY_LOADER, null, MainActivity.this);
                //remove this if spinner is needed
                ActivitySpinner.setVisibility(View.GONE);
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

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {


        String[] data = {
                    CategoryContract.CategoryEntry._ID,
                    CategoryContract.CategoryEntry.COLUMN_IMAGE_PATH,
                    CategoryContract.CategoryEntry.COLUMN_CONTENT_EN,
                    CategoryContract.CategoryEntry.COLUMN_CONTENT_HI
        };

        return new CursorLoader(
                this,
                CategoryContract.CategoryEntry.CONTENT_URI,
                data,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        categoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        categoryAdapter.swapCursor(null);
    }

    public void firstTimeUser() {
        String key = "FIRST_TIME_USER";
        if(getPreferences(Context.MODE_PRIVATE).getBoolean(key, true)) {
            /** first time dialog box*/

            AlertDialog.Builder builder;
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
            }else {
                builder = new AlertDialog.Builder(this);
            }
            builder.setTitle("First time User")
            .setMessage("WellCome, Keep your Internet Connection On, Data is Loading...\n" +
                    "अपना इंटरनेट कनेक्शन चालू रखें, डेटा लोड हो रहा है ....")
            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Handler handler = new Handler();

                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            getLoaderManager().restartLoader(CATEGORY_LOADER, null, MainActivity.this);
                            categoryAdapter.notifyAdapter();
                        }
                    }, 3000);
                }
            }).setIcon(R.drawable.app_ic_launcher_splash).show();

            getPreferences(Context.MODE_PRIVATE).edit().putBoolean(key, false).apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_back:
                drawer.closeDrawer(Gravity.LEFT);
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
        }
        return true;
    }

    @Override
    protected void onResume() {
        overridePendingTransition(R.anim.slide_in_bottum ,R.anim.slide_out_bottom);
        super.onResume();
    }
}
