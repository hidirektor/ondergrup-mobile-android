package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPasswordSelection extends AppCompatActivity {
    public Util util;

    public String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_selection);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            userName = intent.getStringExtra("username");
        }
    }

    public void sendOTPWithSMS(View view) {
        Toast.makeText(ForgetPasswordSelection.this, "SMS sistemimiz henüz aktif değil!", Toast.LENGTH_SHORT).show();
    }

    public void sendOTPWithEmail(View view) {
        if(!userName.equals(null)) {
            String userTypeUrl = util.BASE_URL + util.profileInfoURLPrefix + ":Email";
            String jsonProfileInfoBody = "{\"Username\": \"" + userName + "\"}";

            HTTP http = new HTTP(this);
            http.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String email = response.getString("Email");

                        if(!email.equals(null)) {
                            sendOTP(email);
                        } else {
                            Toast.makeText(ForgetPasswordSelection.this, "E-Posta adresi alınamadı!", Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    Toast.makeText(ForgetPasswordSelection.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(ForgetPasswordSelection.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendOTP(String email) {
        String otpUrl = util.BASE_URL + util.otpURLPrefix;
        String jsonOTPBody = "{\"Email\": \"" + email + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(otpUrl, jsonOTPBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String otpCode = response.getString("otpCode");

                    if(!otpCode.equals(null)) {
                        Intent intent = new Intent(ForgetPasswordSelection.this, ForgetPasswordVerification.class);
                        intent.putExtra("otpcode", otpCode);
                        intent.putExtra("eMail", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(ForgetPasswordSelection.this, "OTP kodu alınamadı!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(ForgetPasswordSelection.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void callBackScreenFromMakeSelection(View view) {
        Intent intent = new Intent(ForgetPasswordSelection.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
