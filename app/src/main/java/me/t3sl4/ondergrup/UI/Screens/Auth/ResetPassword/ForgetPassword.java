package me.t3sl4.ondergrup.UI.Screens.Auth.ResetPassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.UI.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPassword extends AppCompatActivity {
    private TextInputLayout forgetPassUsername;
    private TextInputEditText forgetPassUsernameText;
    private Button forgetPassButton;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        uyariDiyalog = new Dialog(this);

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        forgetPassUsername = findViewById(R.id.forgetPassUsername);
        forgetPassUsernameText = forgetPassUsername.findViewById(R.id.forgetPassUsernameText);
        forgetPassButton = findViewById(R.id.forgetPassButton);
    }

    private void buttonClickListeners() {
        forgetPassButton.setOnClickListener(v -> {
            if(!Util.isEmpty(forgetPassUsernameText)) {
                String username = forgetPassUsernameText.getText().toString();
                UserService.checkUser(this, username, () -> {
                    Intent intent = new Intent(ForgetPassword.this, ForgetPasswordSelection.class);
                    intent.putExtra("username", username);
                    startActivity(intent);
                    finish();
                }, () -> {
                    Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.user_not_found));
                });
            } else {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.empty_username));
            }
        });
    }

    public void callBackScreenFromForgetPassword(View view) {
        Intent intent = new Intent(ForgetPassword.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
