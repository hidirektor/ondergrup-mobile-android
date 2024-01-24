package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUser;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserListScreen;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class EditSubScreen extends AppCompatActivity {
    public Util util;
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

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_edit);

        util = new Util(getApplicationContext());

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
