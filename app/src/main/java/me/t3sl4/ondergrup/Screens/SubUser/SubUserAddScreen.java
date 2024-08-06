package me.t3sl4.ondergrup.Screens.SubUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.Requests.SubUser.SubUserService;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserAddScreen extends AppCompatActivity {
    public User receivedUser;

    private Button signUp;
    private EditText editTextNickname;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextNameSurname;
    private EditText editTextPhone;
    private EditText editTextCompany;

    private ImageView backButton;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subuser_add);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();

        buttonClickListeners();

        PasswordFieldTouchListener.setChangeablePasswordField(editTextPassword, getApplicationContext());
    }

    private void initializeComponents() {
        editTextNickname = findViewById(R.id.editTextNickname);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNameSurname = findViewById(R.id.editTextNameSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCompany = findViewById(R.id.editTextCompany);

        signUp = findViewById(R.id.signUp);
        backButton = findViewById(R.id.backButton);
    }

    private void buttonClickListeners() {
        signUp.setOnClickListener(v -> sendRegisterRequest());

        backButton.setOnClickListener(v -> finish());
    }

    private void sendRegisterRequest() {
        String userName = editTextNickname.getText().toString();
        String email = editTextMail.getText().toString();
        String password = editTextPassword.getText().toString();
        String nameSurname = editTextNameSurname.getText().toString();
        String phone = editTextPhone.getText().toString();
        String companyName = editTextCompany.getText().toString();

        if(checkFields(userName, email, password, nameSurname, phone, companyName)) {

            SubUserService.createSubUser(this, UserDataService.getUserID(this), userName, "NORMAL", nameSurname, email, phone, companyName, password, () -> {
                Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_added));
                new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 1000);
            });
        } else {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_empty_fields));
        }
    }

    private boolean checkFields(String userName, String email, String password, String nameSurname, String phone, String companyName) {
        return !userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nameSurname.isEmpty() && !phone.isEmpty() && !companyName.isEmpty();
    }
}
