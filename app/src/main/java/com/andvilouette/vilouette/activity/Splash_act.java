package com.andvilouette.vilouette.activity;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.Window;

import com.andvilouette.vilouette.R;

public class Splash_act extends AppCompatActivity {
    String sname, suser, spass, strname, strlstname, selectedtext_str1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        setContentView(R.layout.activity_splash_act);
        SharedPreferences sp = getSharedPreferences("save_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        suser = sp.getString("email", null);
        spass = sp.getString("password", null);
        sname = sp.getString("name", null);
        new Handler().postDelayed(new Runnable() {


            @Override

            public void run() {

                if ((suser == null) && (spass == null)) {
                    Intent i = new Intent(Splash_act.this, Splash2_act.class);

                    startActivity(i);

                    // close this activity

                    finish();

                } else {
                    Intent i = new Intent(Splash_act.this, Homenav_act.class);

                    startActivity(i);

                    // close this activity

                    finish();

                }


            }

        }, 3*1000); // wait for 5 seconds

    }
}