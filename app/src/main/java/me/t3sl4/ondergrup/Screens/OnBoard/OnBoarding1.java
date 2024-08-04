package me.t3sl4.ondergrup.Screens.OnBoard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;

public class OnBoarding1 extends AppCompatActivity {

    TextView atlaButton1;
    ImageView nextButton1;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);

        initializeComponents();

        buttonClickListeners();

        setOnBoardingState();
    }

    private void initializeComponents() {
        atlaButton1 = findViewById(R.id.atlaButton);
        nextButton1 = findViewById(R.id.nextButton);
    }

    private void buttonClickListeners() {
        atlaButton1.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoarding1.this, LoginScreen.class);
            startActivity(intent);
            finish();
        });

        nextButton1.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoarding1.this, OnBoarding2.class);
            startActivity(intent);
            finish();
        });

        nextButton1.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }

            return false;
        });
    }

    private void setOnBoardingState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
    }
}