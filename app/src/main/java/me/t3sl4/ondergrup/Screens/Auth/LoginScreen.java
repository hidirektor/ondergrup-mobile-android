package me.t3sl4.ondergrup.Screens.Auth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.ResetPassword.ForgetPassword;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardEngineerScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardSysOpScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardTechnicianScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class LoginScreen extends AppCompatActivity {

    private EditText editTextTextPersonName;
    private EditText editTextTextPassword;
    private Button registerButton;
    private Button resetPassButton;
    private ImageView loginButton;

    private Dialog uyariDiyalog;
    private boolean isPasswordVisible = false;

    public Util util;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        resetPassButton = findViewById(R.id.resetPassButton);

        loginButton.setOnClickListener(v -> sendLoginRequest());

        loginButton.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }

            return false;
        });

        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
        });

        resetPassButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, ForgetPassword.class);
            startActivity(intent);
            finish();
        });

        editTextTextPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextTextPassword.getRight() - editTextTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        int drawableResId = isPasswordVisible ? R.drawable.field_password_hide : R.drawable.field_password_show;
        setPasswordVisibility(isPasswordVisible);
        updatePasswordToggleIcon(drawableResId);
    }

    private void setPasswordVisibility(boolean visible) {
        int inputType = visible ? android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                : android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
        editTextTextPassword.setInputType(inputType);
        editTextTextPassword.setSelection(editTextTextPassword.getText().length());
    }

    private void updatePasswordToggleIcon(@DrawableRes int drawableResId) {
        Drawable[] drawables = editTextTextPassword.getCompoundDrawablesRelative();
        drawables[2] = getResources().getDrawable(drawableResId, getApplicationContext().getTheme());
        editTextTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    private void sendLoginRequest() {
        String username = editTextTextPersonName.getText().toString();
        String password = editTextTextPassword.getText().toString();

        String authenticationUrl = util.BASE_URL + util.loginURLPrefix;

        String jsonLoginBody = "{\"Username\": \"" + username + "\", \"Password\": \"" + password + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(authenticationUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                getUserType(username);
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı adı veya şifreniz hatalı. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
            }
        });
    }

    private void getUserType(String username) {
        String userTypeUrl = util.BASE_URL + util.profileInfoURLPrefix + ":Role";
        String jsonProfileInfoBody = "{\"Username\": \"" + username + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String role = response.getString("Role");
                    initUser(username, role);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Profil bilgileri alınırken hata meydana geldi. Lütfen tekrar dene.");
            }
        });
    }

    public void initUser(String username, String role) {
        String reqUrl = util.BASE_URL + util.wholeProfileURLPrefix;

        String jsonLoginBody = "{\"username\": \"" + username + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(reqUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                Log.d("Resp", String.valueOf(response));
                String role = response.getString("Role");
                String userName = response.getString("UserName");
                String eMail = response.getString("Email");
                String nameSurname = response.getString("NameSurname");
                String phoneNumber = response.getString("Phone");
                String company = response.getString("CompanyName");
                String ownerName = response.getString("Owner");
                String createdAt = response.getString("CreatedAt");
                Log.d("User", role + userName + eMail + nameSurname + phoneNumber + company + createdAt);
                util.user = new User(role, userName, eMail, nameSurname, phoneNumber, company, createdAt);
                Log.d("UserTemp", util.user.getUserName());
                util.user.setRole(role);
                util.user.setUserName(userName);
                util.user.seteMail(eMail);
                util.user.setNameSurname(nameSurname);
                util.user.setPhoneNumber(phoneNumber);
                util.user.setCompanyName(company);
                if(ownerName != null) {
                    util.user.setOwnerName(ownerName);
                } else {
                    util.user.setOwnerName(null);
                }
                util.user.setCreatedAt(createdAt);
                redirectBasedRole(role);
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Girmiş olduğun kullanıcı adı veya şifre hatalı. Lütfen kontrol edip tekrar dene.");
            }
        });
    }

    public void redirectBasedRole(String role) {
        Intent intent = null;

        switch (role) {
            case "NORMAL":
                intent = new Intent(LoginScreen.this, DashboardUserScreen.class);
                intent.putExtra("user", util.user);
                break;
            case "TECHNICIAN":
                intent = new Intent(LoginScreen.this, DashboardTechnicianScreen.class);
                intent.putExtra("user", util.user);
                break;
            case "ENGINEER":
                intent = new Intent(LoginScreen.this, DashboardEngineerScreen.class);
                intent.putExtra("user", util.user);
                break;
            case "SYSOP":
                intent = new Intent(LoginScreen.this, DashboardSysOpScreen.class);
                intent.putExtra("user", util.user);
                break;
            default:
                util.showErrorPopup(uyariDiyalog, "Desteklenmeyen bir kullanıcı rolüne sahipsin. Lütfen iletişime geç.");
                break;
        }

        if (intent != null) {
            startActivity(intent);
            finish();
        }
    }
}
