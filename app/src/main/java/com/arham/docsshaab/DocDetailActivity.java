package com.arham.docsshaab;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.bumptech.glide.Glide;

public class DocDetailActivity extends AppCompatActivity {

    TextView detail_text;
    ImageView detail_img;
    Toolbar toolBar;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_detail);
        //supportPostponeEnterTransition();

        Bundle extras = getIntent().getExtras();
        detail_text = (TextView) findViewById(R.id.detail_text);
        detail_img = (ImageView) findViewById(R.id.detail_img);
        toolBar = (Toolbar) findViewById(R.id.d_tool_bar);

        title = extras.getString("TITLE");

        toolBar.setNavigationIcon(R.drawable.back_toolbar);
        toolBar.setTitle(title);
        setSupportActionBar(toolBar);
        if(getSupportActionBar() != null)
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        detail_text.setText(extras.getString("DETAIL_TEXT"));
        detail_img.setImageResource(R.drawable.loading);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            String imageTransitionName = extras.getString("DETAIL_IMAGE");
            detail_img.setTransitionName(imageTransitionName);
        }

        Glide.with(this)
                .load(extras.getString("DETAIL_IMAGE"))
                .into(detail_img);

        listener();
    }

    public void listener() {
        toolBar.getChildAt(2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public String StringArrayToString(String[] textTitle) {
        String result = "";
        for(int i=0;i<textTitle.length;i++) {
            if(i != 0)
                result += " ";
            result +=  textTitle[i];
        }
        return result;
    }
}