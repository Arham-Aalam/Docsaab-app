package com.arham.docsshaab;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.arham.docsshaab.sync.DocsaabSyncAdapter;

public class SplashActivity extends AppCompatActivity {

    ImageView splashImage;
    Animation animation, slideUpAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        DocsaabSyncAdapter.initializeSyncAdapter(this);

        splashImage = (ImageView) findViewById(R.id.splashImage);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        splashImage.startAnimation(animation);

        /**
        slideUpAnimation = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        splashImage.startAnimation(slideUpAnimation);
        findViewById(R.id.splashImage).startAnimation(slideUpAnimation);
        */


            Thread time = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(3000);
                    String key = "FIRST_TIME_DOCSAAB_WELCOME";
                    if(getPreferences(Context.MODE_PRIVATE).getBoolean(key, true)) {
                        getPreferences(Context.MODE_PRIVATE).edit().putBoolean(key, false).apply();
                        Intent intent = new Intent(getApplicationContext(), WellComeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        time.start();
    }
}
