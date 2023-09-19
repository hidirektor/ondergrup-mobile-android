package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chaos.view.PinView;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPasswordVerification extends AppCompatActivity {
    public Util util;

    public String otpCode;
    public String eMail;

    private PinView enteredOTP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_verification);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            otpCode = intent.getStringExtra("otpcode");
            eMail = intent.getStringExtra("eMail");
        }

        enteredOTP = findViewById(R.id.enteredOTP);
    }

    public void checkOTP(View view) {
        String girilenKod = enteredOTP.getText().toString();

        if(!girilenKod.equals(null)) {
            if(!otpCode.equals(null)) {
                Log.d("OTP", otpCode);
                Log.d("Entered OTP", String.valueOf(girilenKod));
                if(girilenKod.equals(otpCode)) {
                    Intent intent = new Intent(ForgetPasswordVerification.this, ForgetPasswordNewPass.class);
                    intent.putExtra("eMail", eMail);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(ForgetPasswordVerification.this, "Hatalı OTP kodu girdiniz!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ForgetPasswordVerification.this, "OTP kodu hatası!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ForgetPasswordVerification.this, "Lütfen önce OTP kodunu gir!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToHomeFromOTP(View view) {
        Intent intent = new Intent(ForgetPasswordVerification.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
