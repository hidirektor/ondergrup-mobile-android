package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.SysOp;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class EditProfileScreen extends AppCompatActivity {
    public Util util;
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

        util = new Util(getApplicationContext());

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
        String created_at = Util.getCurrentDateTime();
        String password = String.valueOf(passwordEditText.getText());

        if (password.isEmpty()) {
            password = "null";
        }

        String registerJsonBody =
                "{" +
                        "\"UserName\":\"" + kullaniciAdi.getText() + "\"," +
                        "\"Email\":\"" + eMail.getText() + "\"," +
                        "\"Password\":\"" + password + "\"," +
                        "\"NameSurname\":\"" + nameSurname.getText() + "\"," +
                        "\"Phone\":\"" + phone.getText() + "\"," +
                        "\"CompanyName\":\"" + companyName.getText() + "\"" +
                        "}";

        sendUpdateRequest(registerJsonBody, String.valueOf(kullaniciAdi.getText()));
    }

    private void sendUpdateRequest(String jsonBody, String username) {
        String updateProfileUrl = util.BASE_URL + util.updateProfileURLPrefix;

        HTTP.sendRequest(updateProfileUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                finish();
                updateUserObject(receivedUser);
                startActivity(getIntent().putExtra("user", receivedUser));
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Hata", " " + errorMessage);
                util.showErrorPopup(uyariDiyalog, "Profil güncellenirken hata meydana geldi. Lütfen tekrar dene.");
            }
        }, Volley.newRequestQueue(this));
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
