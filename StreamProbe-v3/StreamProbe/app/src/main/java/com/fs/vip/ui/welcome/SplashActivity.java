package com.fs.vip.ui.welcome;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fs.vip.ui.main.MainActivity;
import com.fs.vip.ui.personal.AppsInfoActivity;
import com.fs.vip.utils.SharedPreferencesUtil;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if ("true".equals(SharedPreferencesUtil.getInstance().getString("welcomed"))){
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
        }else{
            Intent intent = new Intent(SplashActivity.this, WelcomActivity.class);
            startActivity(intent);
        }
        finish();
    }
}
