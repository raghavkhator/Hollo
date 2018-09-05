package com.iecsemanipal.holocaust.activities;
/** modified by Shashank, Roshan, Raghav on 05/09/2018*/
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.iecsemanipal.holocaust.R;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);
        final SharedPreferences.Editor edit = prefs.edit();
        edit.apply();

        final ImageView logoExt = (ImageView)findViewById(R.id.splash_logo_ext);
        final ImageView logoInt = (ImageView)findViewById(R.id.splash_logo_int);

        final EditText login = (EditText)findViewById(R.id.et_login);
        final EditText pass = (EditText)findViewById(R.id.et_pass);
        final Button login_bt = (Button)findViewById(R.id.button_login);
        login.setText(prefs.getString("logIn", ""));
        pass.setText(prefs.getString("pass", ""));
        logoExt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_shrink));
        logoInt.setAnimation(AnimationUtils.loadAnimation(this, R.anim.logo_tilt_grow));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
//                logoExt.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                        startActivity(intent);
//                    }
//                });
                logoInt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        finish();
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.activity_hold, R.anim.activity_shrink_fade);
                        return true;
                    }
                });
                login_bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        boolean enter = login(login.getText().toString(), pass.getText().toString());
                        edit.putString("logIn", login.getText().toString());
                        edit.putString("pass", pass.getText().toString());
                        edit.apply();
                        if(enter){
                            finish();
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "Login failed", Toast.LENGTH_LONG).show();
                        }
                        overridePendingTransition(R.anim.activity_hold, R.anim.activity_shrink_fade);
                    }
                });
                //finish();
                //Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                //startActivity(intent);
                overridePendingTransition(R.anim.activity_hold, R.anim.activity_shrink_fade);
            }
        }, 1000);
    }

    private boolean login(String id, String pass) {

        return true;
    }

}
