package com.arham.docsshaab;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.database.DataSetObserver;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CateListActivity extends AppCompatActivity {

    Toolbar ActivityToolbar;
    Spinner ActivitySpinner;
    ArrayAdapter<CharSequence> ActivityAdapter;
    TextView titleText;

    int cat_id;
    int sub_cat_id;
    String _ID = "_ID";
    String cat_ID = "CAT_ID";
    String LANG_NAME = "LANG_NAME";
    String titleName;

    CateListAdapter listAdapter;
    RecyclerView recyclerView;

    ProgressBar p_bar;
    Activity thisActivity;

    private BroadcastReceiver networkChangeReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Toast toast= Toast.makeText(getApplicationContext(),
                    "Connected to Network", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM|Gravity.CENTER, 0, 0);
            toast.show();

            recyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);
            recyclerView.setHasFixedSize(true);

            listAdapter = new CateListAdapter(thisActivity, cat_id, sub_cat_id, p_bar);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));

            recyclerView.setAdapter(listAdapter);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cate_list);

        //checking network
        new NetworkChecker().checkNetworkStatus(this);
        thisActivity = this;

        ActivityToolbar =  (Toolbar) findViewById(R.id.toolbar);
        ActivitySpinner = (Spinner) findViewById(R.id.spinner);
        titleText = (TextView) findViewById(R.id.list_heading);
        p_bar = (ProgressBar) findViewById(R.id.p_bar);

        ActivityToolbar.setNavigationIcon(R.drawable.arrow_back);
        ActivityToolbar.setTitle(R.string.app_name);
        //ActivityToolbar.getBackground().setAlpha(0);
        ActivityAdapter = ArrayAdapter.createFromResource(this, R.array.languages, R.layout.drop_menu);
        ActivityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ActivitySpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ActivitySpinner.setAdapter(ActivityAdapter);

        Intent receivedIntent = getIntent();
        if(receivedIntent.hasExtra(_ID)) {
            this.cat_id = getIntent().getExtras().getInt(_ID);
            this.titleName = getIntent().getExtras().getString(LANG_NAME);
            this.sub_cat_id = getIntent().getExtras().getInt(cat_ID);
            if(!(getIntent().getExtras().getString("choosed_lang").matches(ActivitySpinner.getSelectedItem().toString()))) {
                int pos = ActivitySpinner.getSelectedItemPosition();
                ActivitySpinner.setSelection(++pos%2);
            }
            titleText.setText(titleName);
        }
        listeners();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeReceiver, intentFilter);
    }

    private void listeners() {
        ActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(listAdapter != null) {
                    listAdapter.setLang(ActivitySpinner.getSelectedItem().toString());
                }
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
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(networkChangeReceiver);
    }
}

