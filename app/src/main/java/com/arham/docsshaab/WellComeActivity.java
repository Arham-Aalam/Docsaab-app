package com.arham.docsshaab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

public class WellComeActivity extends AppCompatActivity {

    Animation aniWelcome, welcomeDesc1, welcomeDesc2;
    TextView welcomeTxt;
    Button welcomeButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_well_come);
        aniWelcome = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up);
        welcomeDesc1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        welcomeDesc2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);

        welcomeTxt = (TextView) findViewById(R.id.txt_slide_up);
        welcomeButton = (Button) findViewById(R.id.welcome_btn);

        welcomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getApplicationContext(), MainActivity.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(in);
                finish();
            }
        });

        welcomeTxt.startAnimation(aniWelcome);
        ((TextView)findViewById(R.id.welcomeDesc1)).startAnimation(welcomeDesc1);
        ((TextView)findViewById(R.id.welcomeDesc2)).startAnimation(welcomeDesc2);

    }
}
