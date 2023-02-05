package com.deal.bytee.vendor.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.deal.bytee.vendor.R;
import com.deal.bytee.vendor.Utils.App;
import com.deal.bytee.vendor.Utils.MySharedPreferences;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.main));
        }
        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                String islogin = "";
                if (App.sharedPreferences.chk(MySharedPreferences.isLogin)) {
                    islogin = App.sharedPreferences.getKey(MySharedPreferences.isLogin);
                }
                if (islogin.equals(MySharedPreferences.YES)) {
                    Intent mainIntent = new Intent(SplashActivity.this,DashBoardActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }else {
                    Intent mainIntent = new Intent(SplashActivity.this,LoginActivity.class);
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }
        }, 500);
    }
}