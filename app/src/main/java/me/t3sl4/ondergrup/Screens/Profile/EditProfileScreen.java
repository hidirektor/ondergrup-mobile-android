package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.SysOp;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;

public class EditProfileScreen extends AppCompatActivity {
    public User receivedUser;
    public User mainUser;

    public String incomeScreen;

    private ImageView backButton;

    private EditText nameSurname;
    private EditText eMail;
    private EditText kullaniciAdi;
    private EditText phone;
    private EditText companyName;
    private EditText passwordEditText;
    private LinearLayout showProfile;
    private Button updateProfileButton;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_edit);

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        mainUser = intent.getParcelableExtra("mainUser");
        incomeScreen = intent.getStringExtra("incomeScreen");

        backButton = findViewById(R.id.backButton);

        nameSurname = findViewById(R.id.editTextNameSurname);
        eMail = findViewById(R.id.editTextMail);
        kullaniciAdi = findViewById(R.id.editTextNickname);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.editTextCompany);
        passwordEditText = findViewById(R.id.editTextPassword);

        showProfile = findViewById(R.id.showProfileLayout);
        updateProfileButton = findViewById(R.id.button3);

        if(mainUser != null) {
            showProfile.setVisibility(View.GONE);
        }

        if(receivedUser.getOwnerName() != null) {
            companyName.setInputType(InputType.TYPE_NULL);
        }

        showProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(EditProfileScreen.this, ProfileScreen.class);
            if(mainUser != null) {
                profileIntent.putExtra("user", mainUser);
            } else {
                profileIntent.putExtra("user", receivedUser);
            }

            startActivity(profileIntent);
            finish();
        });

        updateProfileButton.setOnClickListener(v -> {
            updateWholeProfile();
        });

        backButton.setOnClickListener(v -> finish());

        PasswordFieldTouchListener.setChangeablePasswordField(passwordEditText, getApplicationContext());

        setUserInfo();
    }

    public void setUserInfo() {
        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        kullaniciAdi.setText(receivedUser.getUserName());
        phone.setText(receivedUser.getPhoneNumber());
        companyName.setText(receivedUser.getCompanyName());
    }

    public void updateWholeProfile() {
        String password = String.valueOf(passwordEditText.getText());

        if (password.isEmpty()) {
            password = null;
        }

        UserService.updateProfile(this, UserDataService.getUserID(this), nameSurname.getText().toString(), eMail.getText().toString(), companyName.getText().toString(), password, phone.getText().toString(), () -> {
            finish();
            updateUserObject(receivedUser);
            startActivity(getIntent().putExtra("user", receivedUser));
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent profileIntent = null;
        if(incomeScreen != null) {
            profileIntent = new Intent(EditProfileScreen.this, SysOp.class);
            profileIntent.putExtra("user", mainUser);
        } else {
            profileIntent = new Intent(EditProfileScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
        }

        startActivity(profileIntent);
        finish();
    }

    public void updateUserObject(User receivedUser) {
        receivedUser.setNameSurname(String.valueOf(nameSurname.getText()));
        receivedUser.seteMail(String.valueOf(eMail.getText()));
        receivedUser.setPhoneNumber(String.valueOf(phone.getText()));
        receivedUser.setCompanyName(String.valueOf(companyName.getText()));
    }
}
