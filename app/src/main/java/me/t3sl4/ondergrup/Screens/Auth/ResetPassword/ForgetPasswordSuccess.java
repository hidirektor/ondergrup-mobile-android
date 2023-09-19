package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;

public class ForgetPasswordSuccess extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_success);
    }

    public void backToLogin(View view) {
        Intent intent = new Intent(ForgetPasswordSuccess.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
