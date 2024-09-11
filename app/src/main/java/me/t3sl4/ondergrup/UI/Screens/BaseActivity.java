package me.t3sl4.ondergrup.UI;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Token.TokenService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;
import me.t3sl4.ondergrup.Util.Util;

public class BaseActivity extends AppCompatActivity {
    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        uyariDiyalog = new Dialog(this);

        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.postDelayed(() -> UserService.getProfile(BaseActivity.this, UserDataService.getUserID(BaseActivity.this), new Runnable() {
                        @Override
                        public void run() {
                            Util.redirectBasedRole(BaseActivity.this, true, uyariDiyalog);
                        }
                    }, () -> {
                        refreshTokenAndRetry();
                    }), 10);
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        };

        getOnBackPressedDispatcher().addCallback(this, callback);
    }

    private void refreshTokenAndRetry() {
        TokenService.refreshToken(this, () -> UserService.getProfile(BaseActivity.this, UserDataService.getUserID(BaseActivity.this), () -> {
            Util.redirectBasedRole(BaseActivity.this, true, uyariDiyalog);
        }, () -> {
            redirectToLogin();
        }), () -> {
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent intent = new Intent(BaseActivity.this, LoginScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}