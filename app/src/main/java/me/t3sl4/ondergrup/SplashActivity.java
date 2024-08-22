package me.t3sl4.ondergrup;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

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
    private Dialog uyariDiyalog;
    private ImageView logoImageView;
    private Animation fadeIn;
    private Animation fadeOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoImageView = findViewById(R.id.logo);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, false);

        Util.setSystemLanguage(this);

        uyariDiyalog = new Dialog(this);

        if (isFirstTime) {
            setupOnboarding();
        } else {
            startLoadingAnimation();
            redirectToMainActivity();
        }
    }

    private void startLoadingAnimation() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                logoImageView.startAnimation(fadeOut);
                fadeOut.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        logoImageView.startAnimation(fadeIn);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {
                    }
                });
            }
        }, 0);

        fadeIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logoImageView.startAnimation(fadeOut);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
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
                stopLoadingAnimation();
                Util.redirectBasedRole(SplashActivity.this, true, uyariDiyalog);
            }
        }, () -> {
            stopLoadingAnimation();
            refreshTokenAndRetry();
        }), WAITING_TIME);
    }

    private void refreshTokenAndRetry() {
        TokenService.refreshToken(this, () -> UserService.getProfile(SplashActivity.this, UserDataService.getUserID(SplashActivity.this), () -> {
            stopLoadingAnimation();
            Util.redirectBasedRole(SplashActivity.this, true, uyariDiyalog);
        }, () -> {
            stopLoadingAnimation();
            redirectToLogin();
        }), () -> {
            stopLoadingAnimation();
            redirectToLogin();
        });
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginScreen.class);
        startActivity(loginIntent);
        finish();
    }

    private void stopLoadingAnimation() {
        logoImageView.clearAnimation();
    }
}