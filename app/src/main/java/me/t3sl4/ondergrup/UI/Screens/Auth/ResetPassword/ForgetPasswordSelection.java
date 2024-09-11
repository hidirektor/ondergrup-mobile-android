package me.t3sl4.ondergrup.UI.Screens.Auth.ResetPassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.UI.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.HTTP.Requests.OTP.OTPService;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPasswordSelection extends AppCompatActivity {
    public String userName;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_selection);

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("username");
        }
    }

    public void sendOTPWithSMS(View view) {
        Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.sms_not_active));
    }

    public void sendOTPWithEmail(View view) {
        OTPService.sendMail(userName, otpSentTime -> {
            Intent intent = new Intent(ForgetPasswordSelection.this, ForgetPasswordVerification.class);
            intent.putExtra("otpSentTime", otpSentTime);
            intent.putExtra("userName", userName);
            startActivity(intent);
            finish();
        });
    }

    public void callBackScreenFromMakeSelection(View view) {
        Intent intent = new Intent(ForgetPasswordSelection.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
