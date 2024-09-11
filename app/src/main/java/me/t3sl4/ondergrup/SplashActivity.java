package me.t3sl4.ondergrup;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.onesignal.Continue;
import com.onesignal.OneSignal;
import com.onesignal.debug.LogLevel;

import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.Components.Sneaker.Sneaker;
import me.t3sl4.ondergrup.UI.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.UI.Screens.OnBoard.OnBoarding;
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

    String oneSignalAppId = BuildConfig.ONESIGNAL_APP_ID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        logoImageView = findViewById(R.id.logo);
        fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        fadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out);

        // OneSignal initialization for notifications
        OneSignal.getDebug().setLogLevel(LogLevel.VERBOSE);
        OneSignal.initWithContext(this, oneSignalAppId);
        OneSignal.getNotifications().requestPermission(false, Continue.none());

        startLoadingAnimation();

        if (checkLocationPermission()) {
            if (checkNotificationPermission()) {
                continueAppFlow();
            } else {
                showPermissionPopup(2); // Bildirim izni
            }
        } else {
            showPermissionPopup(1); // Konum izni
        }
    }

    private boolean checkLocationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean checkNotificationPermission() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                101);
    }

    private void requestNotificationPermission() {
        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.POST_NOTIFICATIONS},
                102);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (!checkNotificationPermission()) {
                    showPermissionPopup(2);
                } else {
                    continueAppFlow();
                }
            } else {
                showPermissionPopup(1);
            }
        } else if (requestCode == 102) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                continueAppFlow();
            } else {
                showPermissionPopup(2);
            }
        }
    }

    private void showPermissionPopup(int permStatus) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.permission_explanation_popup, null);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setView(dialogView);

        ImageView permIcon = dialogView.findViewById(R.id.permIcon);
        TextView permTitleText = dialogView.findViewById(R.id.permTitle);
        TextView permDescText = dialogView.findViewById(R.id.permDesc);

        Button acceptButton = dialogView.findViewById(R.id.allowButton);
        Button denyButton = dialogView.findViewById(R.id.denyButton);

        AlertDialog alert = alertBuilder.create();

        String locationPermTitle = getString(R.string.permLocationTitle);
        String locationPermDesc = getString(R.string.permLocationDesc);

        String notificationPermTitle = getString(R.string.permNotificationTitle);
        String notificationPermDesc = getString(R.string.permNotificationDesc);

        if(permStatus == 1) {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_location_perm));
            permTitleText.setText(locationPermTitle);
            permDescText.setText(locationPermDesc);
        } else {
            permIcon.setImageDrawable(getDrawable(R.drawable.ikon_notification_perm));
            permTitleText.setText(notificationPermTitle);
            permDescText.setText(notificationPermDesc);
        }

        alertBuilder.setCancelable(true);
        acceptButton.setOnClickListener(v -> {
            alert.dismiss();
            if (permStatus == 1) {
                requestLocationPermission();
            } else {
                requestNotificationPermission();
            }
        });

        denyButton.setOnClickListener(v -> {
            String permErrorMsg = getString(R.string.locationPermError);
            String errorTitleMsg = getString(R.string.errorTitle);
            if(permStatus == 1) {
                Sneaker.with(this).setTitle(errorTitleMsg).setMessage(permErrorMsg).sneakError();
            }
            alert.dismiss();
        });

        alert.show();
        alert.getWindow().setBackgroundDrawableResource(R.drawable.background_permission_dialog);
    }

    private void continueAppFlow() {
        boolean isFirstTime = SharedPreferencesManager.getSharedPref("isFirstTime", this, true);
        Util.setSystemLanguage(this);

        if(Util.isNetworkAvailable(this)) {
            setupOnboarding();
            /*if (isFirstTime) {
                setupOnboarding();
            } else {
                redirectToMainActivity();
            }*/
        } else {
            String errorTitleMsg = getString(R.string.errorTitle);
            String networkErrorMsg = getString(R.string.wifiFailure);
            Sneaker.with(this).setTitle(errorTitleMsg).setMessage(networkErrorMsg).sneakError();
        }
    }

    private void setupOnboarding() {
        SharedPreferencesManager.writeSharedPref("isFirstTime", false, this);
        Intent intent = new Intent(SplashActivity.this, OnBoarding.class);
        startActivity(intent);
        finish();
    }

    private void redirectToMainActivity() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
            UserService.getProfile(SplashActivity.this, UserDataService.getUserID(SplashActivity.this), () -> {
                stopLoadingAnimation();
                Util.redirectBasedRole(SplashActivity.this, true, uyariDiyalog);
            }, () -> {
                stopLoadingAnimation();
                refreshTokenAndRetry();
            });
        }, WAITING_TIME);
    }

    private void refreshTokenAndRetry() {
        TokenService.refreshToken(this, () -> UserService.getProfile(SplashActivity.this, UserDataService.getUserID(SplashActivity.this), () -> {
            stopLoadingAnimation();
            Util.redirectBasedRole(SplashActivity.this, true, uyariDiyalog);
        }, () -> {
            stopLoadingAnimation();
            redirectToLogin();
        }), this::redirectToLogin);
    }

    private void redirectToLogin() {
        Intent loginIntent = new Intent(SplashActivity.this, LoginScreen.class);
        startActivity(loginIntent);
        finish();
    }

    private void startLoadingAnimation() {
        Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(() -> {
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

    private void stopLoadingAnimation() {
        logoImageView.clearAnimation();
    }
}