package me.t3sl4.ondergrup.Screens;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.OnBoard.OnBoarding1;

public class MainActivity extends AppCompatActivity {

    private static final int SPLASH_DELAY = 3500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        boolean isFirstTime = sharedPreferences.getBoolean("isFirstTime", true);

        //setupOnboarding();
        if (isFirstTime) {
            setupOnboarding();
        } else {
            redirectToMainActivity();
        }
    }

    private void setupOnboarding() {
        Intent intent = new Intent(MainActivity.this, OnBoarding1.class);
        startActivity(intent);
        finish();
        setOnBoardingState();
    }

    private void redirectToMainActivity() {
        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, LoginScreen.class);
            startActivity(intent);
            finish();
        }, SPLASH_DELAY);
    }

    private void setOnBoardingState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
    }
}