package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
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
        Util.showErrorPopup(uyariDiyalog, "SMS sistemimiz henüz aktif değil.");
    }

    public void sendOTPWithEmail(View view) {
        if(!userName.equals(null)) {
            /*String userTypeUrl = util.BASE_URL + util.profileInfoURLPrefix + ":Email";
            String jsonProfileInfoBody = "{\"Username\": \"" + userName + "\"}";

            HTTP.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
                @Override
                public void onSuccess(JSONObject response) {
                    try {
                        String email = response.getString("Email");

                        if(!email.equals(null)) {
                            sendOTP(email);
                        } else {
                            util.showErrorPopup(uyariDiyalog, "E-Posta adresi alınamadı. Lütfen tekrar dene.");
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(String errorMessage) {
                    util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. Lütfen bilgilerini kontrol edip tekrar dene.");
                }
            }, Volley.newRequestQueue(this));*/
        } else {
            Util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. Lütfen bilgilerini kontrol edip tekrar dene.");
        }
    }

    public void sendOTP(String email) {
        /*String otpUrl = util.BASE_URL + util.otpURLPrefix;
        String jsonOTPBody = "{\"Email\": \"" + email + "\"}";

        HTTP.sendRequest(otpUrl, jsonOTPBody, new HTTP.HttpRequestCallback() {
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
                        util.showErrorPopup(uyariDiyalog, "OTP kodu alınamadı. Lütfen bilgilerini kontrol edip tekrar dene.");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. Lütfen bilgilerini kontrol edip tekrar dene.");
            }
        }, Volley.newRequestQueue(this));*/
    }

    public void callBackScreenFromMakeSelection(View view) {
        Intent intent = new Intent(ForgetPasswordSelection.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
