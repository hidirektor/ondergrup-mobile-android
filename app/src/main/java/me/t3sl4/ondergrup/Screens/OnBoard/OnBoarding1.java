package me.t3sl4.ondergrup.Screens.OnBoard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;

public class OnBoarding1 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding1);

        TextView atlaButton1 = findViewById(R.id.atlaButton);
        ImageView nextButton1 = findViewById(R.id.nextButton);

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

        setOnBoardingState();
    }

    private void setOnBoardingState() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isFirstTime", false);
        editor.apply();
    }
}