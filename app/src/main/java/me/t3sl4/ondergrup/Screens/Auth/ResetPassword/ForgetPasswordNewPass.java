package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Auth.AuthService;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPasswordNewPass extends AppCompatActivity {
    public String userName;
    public String otpSentTime;

    private TextInputLayout editTextNewPass;
    private TextInputEditText editTextNewPassText;
    private TextInputLayout editTextConfirmNewPass;
    private TextInputEditText editTextConfirmNewPassText;

    private Dialog uyariDiyalog;

    private boolean isPasswordVisibleNormal = false;
    private boolean isPasswordVisibleConfirm = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_new_password);

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("userName");
            otpSentTime = intent.getStringExtra("otpSentTime");
        }

        initializeComponents();

        PasswordFieldTouchListener.setChangeablePasswordFieldLayout(editTextNewPass, getApplicationContext());
        PasswordFieldTouchListener.setChangeablePasswordFieldLayout(editTextConfirmNewPass, getApplicationContext());
    }

    private void initializeComponents() {
        editTextNewPass = findViewById(R.id.editTextNewPass);
        editTextNewPassText = editTextNewPass.findViewById(R.id.editTextNewPassText);
        editTextConfirmNewPass = findViewById(R.id.editTextConfirmNewPass);
        editTextConfirmNewPassText = editTextConfirmNewPass.findViewById(R.id.editTextConfirmNewPassText);
    }

    public void setNewPassword(View view) {
        String firstPass;
        String secondPass;

        if(!editTextNewPassText.getText().equals(null) && !editTextConfirmNewPassText.getText().equals(null)) {
            firstPass = editTextNewPassText.getText().toString();
            secondPass = editTextConfirmNewPassText.getText().toString();

            if(firstPass.equals(secondPass)) {
                AuthService.resetPass(userName, firstPass.toString(), otpSentTime, () -> {
                    Intent intent = new Intent(ForgetPasswordNewPass.this, ForgetPasswordSuccess.class);
                    startActivity(intent);
                    finish();
                });
            } else {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.repeated_pass_not_same));
            }
        } else {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.empty_password));
        }
    }

    public void goToHomeFromSetNewPassword(View view) {
        Intent intent = new Intent(ForgetPasswordNewPass.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
