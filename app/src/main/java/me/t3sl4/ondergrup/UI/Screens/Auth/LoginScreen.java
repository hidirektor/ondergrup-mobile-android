package me.t3sl4.ondergrup.UI.Screens.Auth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.BaseActivity;
import me.t3sl4.ondergrup.UI.Screens.Auth.ResetPassword.ForgetPassword;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Auth.AuthService;
import me.t3sl4.ondergrup.Util.Util;

public class LoginScreen extends BaseActivity {
    //General Components
    private TextView resetPassButton;
    private LinearLayout loginSection;
    private Button loginSectionButton;
    private LinearLayout registerSection;
    private Button registerSectionButton;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uyariDiyalog = new Dialog(this);

        initializeComponents();

        checkWifiStatus();

        buttonClickListeners();

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

        //Login Components
        userNameField_login = findViewById(R.id.userNameField_login);
        passwordField_login = findViewById(R.id.passwordField_login);
        loginButton = findViewById(R.id.loginButton);

        // Dummy data doldurma
        userNameField_login.setText("recep.baskurt@ondergrup.com");
        passwordField_login.setText("asdasd");

        //Register Section:
        nameSurnameField_register = findViewById(R.id.nameSurnameField_register);
        userNameField_register = findViewById(R.id.userNameField_register);
        passwordField_register = findViewById(R.id.passwordField_register);
        mailField_register = findViewById(R.id.mailField_register);
        phoneField_register = findViewById(R.id.phoneField_register);
        companyField_register = findViewById(R.id.companyField_register);
        registerButton = findViewById(R.id.registerButton);
    }

    private void buttonClickListeners() {
        registerButton.setOnClickListener(v -> sendRegisterRequest());

        loginButton.setOnClickListener(v -> sendLoginRequest());

        resetPassButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, ForgetPassword.class);
            startActivity(intent);
            finish();
        });
    }

    private void sendLoginRequest() {
        String username = userNameField_login.getText().toString();
        String password = passwordField_login.getText().toString();

        // Gerçek istek yorum satırına alındı
        /*
        AuthService.login(this, username, password, () -> {
            UserService.getProfile(this, UserDataService.getUserID(this), () -> {
                Util.redirectBasedRole(LoginScreen.this, false, uyariDiyalog);
            }, null);
        }, () -> {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.login_error));
        });
        */

        // Dummy login simülasyonu
        // Kullanıcı adı ve şifre doğruysa başarılı login simülasyonu
        if (username.equals("recep.baskurt@ondergrup.com") && password.equals("asdasd")) {
            // Dummy UserDataService doldurulabilir, ardından yönlendirme yapılır
            UserDataService.setUserID(this, "dummyUserID");
            UserDataService.setUserRole(this, "NORMAL");
            UserDataService.setAccessToken(this, "dummyAccessToken");
            UserDataService.setRefreshToken(this, "dummyRefreshToken");
            Util.redirectBasedRole(LoginScreen.this, false, uyariDiyalog);
        } else {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.login_error));
        }
    }

    private void sendRegisterRequest() {
        String userName = userNameField_register.getText().toString();
        String email = mailField_register.getText().toString();
        String password = passwordField_register.getText().toString();
        String nameSurname = nameSurnameField_register.getText().toString();
        String phone = phoneField_register.getText().toString();
        String companyName = companyField_register.getText().toString();

        if(checkFields(userName, email, password, nameSurname, phone, companyName)) {
            AuthService.register(this, userName, "NORMAL", nameSurname, email, phone, companyName, password, () -> swipeSections(1));
        } else {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.empty_fields));
        }
    }

    private boolean checkFields(String userName, String email, String password, String nameSurname, String phone, String companyName) {
        return !userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nameSurname.isEmpty() && !phone.isEmpty() && !companyName.isEmpty();
    }

    private void checkWifiStatus() {
        boolean networkStatus = Util.isNetworkAvailable(LoginScreen.this);
        String wifiFailureMessage = LoginScreen.this.getResources().getString(R.string.wifiFailure);

        if(!networkStatus) {
            Util.showErrorPopup(uyariDiyalog, wifiFailureMessage);
        }
    }
}
