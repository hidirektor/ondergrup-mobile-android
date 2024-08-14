package me.t3sl4.ondergrup;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.OnBoard.OnBoarding1;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Token.TokenService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;
import me.t3sl4.ondergrup.Util.SharedPreferencesManager;
import me.t3sl4.ondergrup.Util.Util;

public class SplashActivity extends AppCompatActivity {
    private final int WAITING_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, false);

        Util.setSystemLanguage(this);

        if (isFirstTime) {
            setupOnboarding();
        } else {
            redirectToMainActivity();
        }
    }

    private void setupOnboarding() {
        Intent intent = new Intent(SplashActivity.this, OnBoarding1.class);
        startActivity(intent);
        finish();
        SharedPreferencesManager.writeSharedPref("isFirstTime", false, this);
    }

    private void redirectToMainActivity() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> UserService.getProfile(SplashActivity.this, UserDataService.getUserID(SplashActivity.this), new Runnable() {
            @Override
            public void run() {
                Util.redirectBasedRole(SplashActivity.this, true);
            }
        }, () -> {
            if (isHuaweiDevice()) {
                redirectToLogin();
            } else {
                refreshTokenAndRetry();
            }
        }), WAITING_TIME);
    }

    private boolean isHuaweiDevice() {
        return Build.MANUFACTURER.equalsIgnoreCase("HUAWEI");
    }

    private void refreshTokenAndRetry() {
        TokenService.refreshToken(this, () -> UserService.getProfile(SplashActivity.this, UserDataService.getUserID(SplashActivity.this), () -> Util.redirectBasedRole(SplashActivity.this, true), () -> {
            redirectToLogin();
        }), () -> {
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginScreen.class);
        startActivity(loginIntent);
        finish();
    }
}
