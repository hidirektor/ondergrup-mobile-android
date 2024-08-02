package me.t3sl4.ondergrup;

import static me.t3sl4.ondergrup.Service.UserDataService.getUserFromPreferences;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.Engineer;
import me.t3sl4.ondergrup.Screens.Dashboard.SysOp;
import me.t3sl4.ondergrup.Screens.Dashboard.Technician;
import me.t3sl4.ondergrup.Screens.OnBoard.OnBoarding1;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.SharedPreferencesManager;
import me.t3sl4.ondergrup.Util.Util;

public class SplashActivity extends AppCompatActivity {
    private final int WAITING_TIME = 3000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, false);
        Util.loadNewTranslations(SplashActivity.this);

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
        new Handler().postDelayed(() -> {
            redirectBasedRole(UserDataService.getUserFromPreferences(this).getRole());
        }, WAITING_TIME);
    }

    public void redirectBasedRole(String role) {
        Intent intent = null;

        switch (role) {
            case "NORMAL":
                intent = new Intent(SplashActivity.this, me.t3sl4.ondergrup.Screens.Dashboard.User.class);
                intent.putExtra("user", getUserFromPreferences(this));
                break;
            case "TECHNICIAN":
                intent = new Intent(SplashActivity.this, Technician.class);
                intent.putExtra("user", getUserFromPreferences(this));
                break;
            case "ENGINEER":
                intent = new Intent(SplashActivity.this, Engineer
                        .class);
                intent.putExtra("user", getUserFromPreferences(this));
                break;
            case "SYSOP":
                intent = new Intent(SplashActivity.this, SysOp.class);
                intent.putExtra("user", getUserFromPreferences(this));
                break;
            default:
                intent = new Intent(SplashActivity.this, LoginScreen.class);
                startActivity(intent);
                finish();
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
