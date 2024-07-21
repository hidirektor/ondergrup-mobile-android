package me.t3sl4.ondergrup.Screens.Auth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;
import com.zpj.widget.checkbox.ZCheckBox;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.ResetPassword.ForgetPassword;
import me.t3sl4.ondergrup.Screens.Dashboard.Engineer;
import me.t3sl4.ondergrup.Screens.Dashboard.SysOp;
import me.t3sl4.ondergrup.Screens.Dashboard.Technician;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.Component.SharedPreferencesManager;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.Util;

public class LoginScreen extends AppCompatActivity {
    //General Components
    private TextView resetPassButton;
    private LinearLayout loginSection;
    private Button loginSectionButton;
    private LinearLayout registerSection;
    private Button registerSectionButton;
    private ZCheckBox rememberMe;


    //Login Components
    private EditText userNameField_login;
    private EditText passwordField_login;
    private Button loginButton;


    //Register Section:
    private EditText nameSurnameField_register;
    private EditText userNameField_register;
    private EditText passwordField_register;
    private EditText mailField_register;
    private EditText phoneField_register;
    private EditText companyField_register;
    private Button registerButton;

    //Register variables:
    boolean isLogged = false;

    //General variables:
    private Dialog uyariDiyalog;

    public Util util;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);

        initializeComponents();

        checkWifiStatus();

        registerButton.setOnClickListener(v -> sendRegisterRequest());

        loginButton.setOnClickListener(v -> sendLoginRequest());

        resetPassButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, ForgetPassword.class);
            startActivity(intent);
            finish();
        });

        rememberMe.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isLogged) {
                String cipheredPass = getCipheredPass();

                String enteredUsername = userNameField_login.getText().toString();

                Log.d("testusername", enteredUsername);

                SharedPreferencesManager.writeSharedPref("username", enteredUsername, this);
                SharedPreferencesManager.writeSharedPref("password", cipheredPass, this);
                SharedPreferencesManager.writeSharedPref("role", util.user.getRole(), this);
            }
        });

        PasswordFieldTouchListener.setChangeablePasswordField(passwordField_login, getApplicationContext());
        PasswordFieldTouchListener.setChangeablePasswordField(passwordField_register, getApplicationContext());

        sectionPager();
    }

    private void sectionPager() {
        loginSectionButton.setOnClickListener(v -> {
            ButtonManager.orderButtonColorEffect(1, loginSectionButton, registerSectionButton, this);
            loginSection.setVisibility(View.VISIBLE);
            registerSection.setVisibility(View.GONE);
        });

        registerSectionButton.setOnClickListener(v -> {
            ButtonManager.orderButtonColorEffect(2, loginSectionButton, registerSectionButton, this);
            registerSection.setVisibility(View.VISIBLE);
            loginSection.setVisibility(View.GONE);
        });
    }

    private void swipeSections(int selectedSection) {
        if(selectedSection == 1) {
            ButtonManager.orderButtonColorEffect(1, loginSectionButton, registerSectionButton, this);
            loginSection.setVisibility(View.VISIBLE);
            registerSection.setVisibility(View.GONE);
        } else {
            ButtonManager.orderButtonColorEffect(2, loginSectionButton, registerSectionButton, this);
            registerSection.setVisibility(View.VISIBLE);
            loginSection.setVisibility(View.GONE);
        }
    }

    private void initializeComponents() {
        //General Components
        resetPassButton = findViewById(R.id.resetPassButton);
        loginSection = findViewById(R.id.loginSection);
        loginSectionButton = findViewById(R.id.loginSectionButton);
        registerSection = findViewById(R.id.registerSection);
        registerSectionButton = findViewById(R.id.registerSectionButton);
        rememberMe = findViewById(R.id.beniHatirlaCheckBox);

        //Login Components
        userNameField_login = findViewById(R.id.userNameField_login);
        passwordField_login = findViewById(R.id.passwordField_login);
        loginButton = findViewById(R.id.loginButton);

        //Register Section:
        nameSurnameField_register = findViewById(R.id.nameSurnameField_register);
        userNameField_register = findViewById(R.id.userNameField_register);
        passwordField_register = findViewById(R.id.passwordField_register);
        mailField_register = findViewById(R.id.mailField_register);
        phoneField_register = findViewById(R.id.phoneField_register);
        companyField_register = findViewById(R.id.companyField_register);
        registerButton = findViewById(R.id.registerButton);
    }

    private void sendLoginRequest() {
        String username = userNameField_login.getText().toString();
        String password = passwordField_login.getText().toString();

        String authenticationUrl = util.BASE_URL + util.loginURLPrefix;

        String jsonLoginBody = "{\"Username\": \"" + username + "\", \"Password\": \"" + password + "\"}";

        HTTP.sendRequest(authenticationUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                isLogged = true;
                getUserType(username);
                saveCredentials();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.d("login", authenticationUrl + " " + jsonLoginBody);
                util.showErrorPopup(uyariDiyalog, "Kullanıcı adı veya şifreniz hatalı. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
            }
        }, Volley.newRequestQueue(this));
    }

    private void saveCredentials() {
        if(rememberMe.isChecked()) {
            String cipheredPass = getCipheredPass();

            String enteredUsername = userNameField_login.getText().toString();

            SharedPreferencesManager.writeSharedPref("username", enteredUsername, this);
            SharedPreferencesManager.writeSharedPref("password", cipheredPass, this);
            SharedPreferencesManager.writeSharedPref("role", util.user.getRole(), this);
        }
    }

    private String getCipheredPass() {
        final String[] cipheredPass = {""};

        String enteredPass = passwordField_login.getText().toString();
        String cipheredPassUrl = util.BASE_URL + util.getPassURLPrefix;

        String jsonLoginBody = "{\"Password\": \"" + enteredPass + "\"}";

        HTTP.sendRequest(cipheredPassUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) throws JSONException {
                try {
                    cipheredPass[0] = response.getString("pass");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                //util.showErrorPopup(uyariDiyalog, "Kullanıcı adı veya şifreniz hatalı. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
            }
        }, Volley.newRequestQueue(this));
        return cipheredPass[0];
    }

    private void getUserType(String username) {
        String userTypeUrl = util.BASE_URL + util.profileInfoURLPrefix + ":Role";
        String jsonProfileInfoBody = "{\"Username\": \"" + username + "\"}";

        HTTP.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
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
        }, Volley.newRequestQueue(this));
    }

    public void initUser(String username, String role) {
        String reqUrl = util.BASE_URL + util.wholeProfileURLPrefix;

        String jsonLoginBody = "{\"username\": \"" + username + "\"}";

        HTTP.sendRequest(reqUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
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
                util.user.setOwnerName(ownerName);
                util.user.setCreatedAt(createdAt);
                redirectBasedRole(role);
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Girmiş olduğun kullanıcı adı veya şifre hatalı. Lütfen kontrol edip tekrar dene.");
            }
        }, Volley.newRequestQueue(this));
    }

    public void redirectBasedRole(String role) {
        Intent intent = null;

        switch (role) {
            case "NORMAL":
                intent = new Intent(LoginScreen.this, me.t3sl4.ondergrup.Screens.Dashboard.User.class);
                intent.putExtra("user", util.user);
                break;
            case "TECHNICIAN":
                intent = new Intent(LoginScreen.this, Technician.class);
                intent.putExtra("user", util.user);
                break;
            case "ENGINEER":
                intent = new Intent(LoginScreen.this, Engineer
                        .class);
                intent.putExtra("user", util.user);
                break;
            case "SYSOP":
                intent = new Intent(LoginScreen.this, SysOp.class);
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

    private void sendRegisterRequest() {
        String userName = userNameField_register.getText().toString();
        String email = mailField_register.getText().toString();
        String password = passwordField_register.getText().toString();
        String nameSurname = nameSurnameField_register.getText().toString();
        String phone = phoneField_register.getText().toString();
        String companyName = companyField_register.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String createdAt = sdf.format(new Date());

        if(checkFields(userName, email, password, nameSurname, phone, companyName)) {
            String profilePhotoPath = userName + ".jpg";
            String registerJsonBody =
                    "{" +
                            "\"Role\":\"" + "NORMAL" + "\"," +
                            "\"UserName\":\"" + userName + "\"," +
                            "\"Email\":\"" + email + "\"," +
                            "\"Password\":\"" + password + "\"," +
                            "\"NameSurname\":\"" + nameSurname + "\"," +
                            "\"Phone\":\"" + phone + "\"," +
                            "\"Profile_Photo\":\"" + profilePhotoPath + "\"," +
                            "\"CompanyName\":\"" + companyName + "\"," +
                            "\"Created_At\":\"" + createdAt + "\"" +
                            "}";

            sendRegisterRequestFinal(registerJsonBody, userName);
        } else {
            util.showErrorPopup(uyariDiyalog, "Kayıt olmak için tüm alanları doldurmalısın.");
        }
    }

    private void sendRegisterRequestFinal(String jsonBody, String userName) {
        String registerUrl = util.BASE_URL + util.registerURLPrefix;

        HTTP.sendRequest(registerUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                swipeSections(1);
                util.showSuccessPopup(uyariDiyalog, "Kayıt başarılı giriş yapabilirsiniz.");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kayıt olurken hata meydana geldi. Lütfen tekrar dene.");
            }
        }, Volley.newRequestQueue(this));
    }

    private boolean checkFields(String userName, String email, String password, String nameSurname, String phone, String companyName) {
        return !userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nameSurname.isEmpty() && !phone.isEmpty() && !companyName.isEmpty();
    }

    private void checkWifiStatus() {
        boolean networkStatus = Util.isNetworkAvailable(LoginScreen.this);
        String wifiFailureMessage = LoginScreen.this.getResources().getString(R.string.wifiFailure);

        if(!networkStatus) {
            util.showErrorPopup(uyariDiyalog, wifiFailureMessage);
            //Service başlat ve wifi ağına bağlı olup olmadığını kontrol etsin.
        } else {
            String username = SharedPreferencesManager.getSharedPref("username", this, "");

            if (!TextUtils.isEmpty(username)) {
                String password = SharedPreferencesManager.getSharedPref("username", this, "");
                String role = SharedPreferencesManager.getSharedPref("role", this, "");
                initUser(username, role);
            }
        }
    }
}
