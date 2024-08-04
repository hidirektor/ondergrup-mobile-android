package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.Model.SubUser.SubUser;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserListScreen;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.Requests.SubUser.SubUserService;
import me.t3sl4.ondergrup.Util.Util;

public class EditSubScreen extends AppCompatActivity {
    public User receivedUser;
    public SubUser receivedSubUser;

    private EditText nameSurname;
    private EditText eMail;
    private EditText kullaniciAdi;
    private EditText phone;
    private EditText companyName;
    private EditText passwordEditText;
    private LinearLayout showSubUsers;

    private ImageView backButton;
    private Button editSubUserButton;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_edit);

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        receivedSubUser = intent.getParcelableExtra("subuser");

        nameSurname = findViewById(R.id.editTextNameSurname);
        eMail = findViewById(R.id.editTextMail);
        kullaniciAdi = findViewById(R.id.editTextNickname);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.editTextCompany);
        passwordEditText = findViewById(R.id.editTextPassword);

        showSubUsers = findViewById(R.id.showSubUsersLayout);

        showSubUsers.setOnClickListener(v -> {
            Intent subUserIntent = new Intent(EditSubScreen.this, SubUserListScreen.class);
            subUserIntent.putExtra("user", receivedUser);
            startActivity(subUserIntent);
            finish();
        });

        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(v -> finish());

        editSubUserButton = findViewById(R.id.editSubButton);
        editSubUserButton.setOnClickListener(v -> {
            updateWholeProfile();
        });

        PasswordFieldTouchListener.setChangeablePasswordField(passwordEditText, getApplicationContext());

        setUserInfo();
    }

    public void setUserInfo() {
        nameSurname.setText(receivedSubUser.getNameSurname());
        eMail.setText(receivedSubUser.geteMail());
        kullaniciAdi.setText(receivedSubUser.getUserName());
        phone.setText(receivedSubUser.getPhone());
        companyName.setText(receivedSubUser.getCompanyName());
    }

    public void updateWholeProfile() {
        String password = String.valueOf(passwordEditText.getText());

        if (password.isEmpty()) {
            password = null;
        }

        SubUserService.editSubUser(this, UserDataService.getUserID(this), receivedSubUser.getSubUserID(), kullaniciAdi.getText().toString(), "NORMAL", nameSurname.getText().toString(), eMail.getText().toString(), phone.getText().toString(), companyName.getText().toString(), password, () -> {
            updateUserObject(receivedUser);
            Util.showSuccessPopup(uyariDiyalog, "Alt kullanıcı başarılı bir şekilde güncellendi.");
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                finish();
            }, 1000);
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent profileIntent = new Intent(EditSubScreen.this, SubUserListScreen.class);
        finish();
        startActivity(profileIntent.putExtra("user", receivedUser));
    }

    public void updateUserObject(User receivedUser) {
        receivedUser.setNameSurname(String.valueOf(nameSurname.getText()));
        receivedUser.seteMail(String.valueOf(eMail.getText()));
        receivedUser.setPhoneNumber(String.valueOf(phone.getText()));
        receivedUser.setCompanyName(String.valueOf(companyName.getText()));
    }
}
