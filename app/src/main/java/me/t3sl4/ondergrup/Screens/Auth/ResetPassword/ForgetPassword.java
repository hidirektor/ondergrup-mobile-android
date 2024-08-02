package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
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

        forgetPassUsername = findViewById(R.id.forgetPassUsername);
        forgetPassUsernameText = forgetPassUsername.findViewById(R.id.forgetPassUsernameText);
        forgetPassButton = findViewById(R.id.forgetPassButton);

        forgetPassButton.setOnClickListener(v -> {
            if(!Util.isEmpty(forgetPassUsernameText)) {
                String username = forgetPassUsernameText.getText().toString();
                sendOTP(username);
            } else {
                Util.showErrorPopup(uyariDiyalog, "E-Posta alanı boş olamaz.");
            }
        });
    }

    private void sendOTP(String username) {
        /*String userTypeUrl = util.BASE_URL + util.profileInfoURLPrefix + ":Role";
        String jsonProfileInfoBody = "{\"Username\": \"" + username + "\"}";

        HTTP.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String role = response.getString("Role");

                    if(!role.equals(null)) {
                        Intent intent = new Intent(ForgetPassword.this, ForgetPasswordSelection.class);
                        intent.putExtra("username", username);
                        startActivity(intent);
                        finish();
                    } else {
                        util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. İstersen tekrar deneyebilirsin.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. İstersen tekrar deneyebilirsin.");
            }
        }, Volley.newRequestQueue(this));*/
    }

    public void callBackScreenFromForgetPassword(View view) {
        Intent intent = new Intent(ForgetPassword.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
