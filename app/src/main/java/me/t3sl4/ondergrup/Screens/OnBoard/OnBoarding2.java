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

public class OnBoarding2 extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_boarding2);

        TextView atlaButton2 = findViewById(R.id.atlaButton2);
        ImageView nextButton2 = findViewById(R.id.nextButton);

        atlaButton2.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoarding2.this, LoginScreen.class);
            startActivity(intent);
            finish();
        });

        nextButton2.setOnClickListener(v -> {
            Intent intent = new Intent(OnBoarding2.this, OnBoarding3.class);
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