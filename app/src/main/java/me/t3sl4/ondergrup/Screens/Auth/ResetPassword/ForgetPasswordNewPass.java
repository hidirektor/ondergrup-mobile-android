package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.Util;

public class ForgetPasswordNewPass extends AppCompatActivity {
    public Util util;

    public String eMail;

    private TextInputLayout editTextNewPass;
    private TextInputEditText editTextNewPassText;
    private TextInputLayout editTextConfirmNewPass;
    private TextInputEditText editTextConfirmNewPassText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_new_password);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        if (intent != null) {
            eMail = intent.getStringExtra("eMail");
        }

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
                String otpUrl = util.BASE_URL + util.updatePassURLPrefix;
                String jsonUpdatePassBody = "{\"Email\": \"" + eMail + "\", \"Password\": \"" + firstPass + "\"}";

                HTTP http = new HTTP(this);
                http.sendRequest(otpUrl, jsonUpdatePassBody, new HTTP.HttpRequestCallback() {
                    @Override
                    public void onSuccess(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            if(message.contains("Şifre güncellendi")) {
                                Intent intent = new Intent(ForgetPasswordNewPass.this, ForgetPasswordSuccess.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ForgetPasswordNewPass.this, "Şifre güncellenemedi!", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        Toast.makeText(ForgetPasswordNewPass.this, "Kullanıcı bulunamadı!", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(ForgetPasswordNewPass.this, "Girilen şifreler birbirleriyle uyuşmuyor!", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(ForgetPasswordNewPass.this, "Lütfen iki şifre alanını da doldur!", Toast.LENGTH_SHORT).show();
        }
    }

    public void goToHomeFromSetNewPassword(View view) {
        Intent intent = new Intent(ForgetPasswordNewPass.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}
