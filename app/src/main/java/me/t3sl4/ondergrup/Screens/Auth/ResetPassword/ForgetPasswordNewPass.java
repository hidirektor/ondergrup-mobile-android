package me.t3sl4.ondergrup.Screens.Auth.ResetPassword;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
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

    private Dialog uyariDiyalog;

    private boolean isPasswordVisibleNormal = false;
    private boolean isPasswordVisibleConfirm = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_new_password);

        util = new Util(getApplicationContext());

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        if (intent != null) {
            eMail = intent.getStringExtra("eMail");
        }

        editTextNewPass = findViewById(R.id.editTextNewPass);
        editTextNewPassText = editTextNewPass.findViewById(R.id.editTextNewPassText);
        editTextConfirmNewPass = findViewById(R.id.editTextConfirmNewPass);
        editTextConfirmNewPassText = editTextConfirmNewPass.findViewById(R.id.editTextConfirmNewPassText);

        editTextNewPass.setEndIconOnClickListener(v -> {
            isPasswordVisibleNormal = !isPasswordVisibleNormal;
            updatePasswordVisibilityNormal(editTextNewPassText);
        });
        editTextConfirmNewPass.setEndIconOnClickListener(v -> {
            isPasswordVisibleConfirm = !isPasswordVisibleConfirm;
            updatePasswordVisibilityConfirm(editTextConfirmNewPassText);
        });
    }

    private void updatePasswordVisibilityNormal(TextInputEditText editText) {
        if (isPasswordVisibleNormal) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editTextNewPass.setEndIconDrawable(R.drawable.field_password_hide);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editTextNewPass.setEndIconDrawable(R.drawable.field_password_show);
        }
        editText.setSelection(editText.getText().length());
    }

    private void updatePasswordVisibilityConfirm(TextInputEditText editText) {
        if (isPasswordVisibleConfirm) {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            editTextConfirmNewPass.setEndIconDrawable(R.drawable.field_password_hide);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editTextConfirmNewPass.setEndIconDrawable(R.drawable.field_password_show);
        }
        editText.setSelection(editText.getText().length());
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
                                util.showErrorPopup(uyariDiyalog, "Şifre güncellenirken hata meydana geldi. Lütfen tekrar dene.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage) {
                        util.showErrorPopup(uyariDiyalog, "Kullanıcı bulunamadı. Lütfen tekrar dene.");
                    }
                });
            } else {
                util.showErrorPopup(uyariDiyalog, "Girilen şifreler birbirleriyle uyuşmuyor. Lütfen tekrar dene.");
            }
        } else {
            util.showErrorPopup(uyariDiyalog, "İki şifre alanını da doldurman gerekiyor.");
        }
    }

    public void goToHomeFromSetNewPassword(View view) {
        Intent intent = new Intent(ForgetPasswordNewPass.this, LoginScreen.class);
        startActivity(intent);
        finish();
    }
}