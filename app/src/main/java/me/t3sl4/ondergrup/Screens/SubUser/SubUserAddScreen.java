package me.t3sl4.ondergrup.Screens.SubUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Component.PasswordField.PasswordFieldTouchListener;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserAddScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private Button signUp;
    private EditText editTextNickname;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextNameSurname;
    private EditText editTextPhone;
    private EditText editTextCompany;
    private boolean isPasswordVisible = false;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_add);

        uyariDiyalog = new Dialog(this);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        editTextNickname = findViewById(R.id.editTextNickname);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNameSurname = findViewById(R.id.editTextNameSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCompany = findViewById(R.id.editTextCompany);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(v -> sendRegisterRequest());

        PasswordFieldTouchListener.setChangeablePasswordField(editTextPassword, getApplicationContext());
    }

    private void sendRegisterRequest() {
        String userName = editTextNickname.getText().toString();
        String email = editTextMail.getText().toString();
        String password = editTextPassword.getText().toString();
        String nameSurname = editTextNameSurname.getText().toString();
        String phone = editTextPhone.getText().toString();
        String companyName = editTextCompany.getText().toString();

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
                            "\"OwnerName\":\"" + receivedUser.getUserName() + "\"," +
                            "\"Created_At\":\"" + createdAt + "\"" +
                            "}";

            sendRegisterRequestFinal(registerJsonBody, userName);
        } else {
            util.showErrorPopup(uyariDiyalog, "Alt kullanıcı eklemek için tüm alanları doldurmalısın.");
        }
    }

    private void sendRegisterRequestFinal(String jsonBody, String userName) {
        String registerUrl = util.BASE_URL + util.addSubURLPrefix;

        HTTP.sendRequest(registerUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                util.showSuccessPopup(uyariDiyalog, "Alt Kullanıcı Başarıyla Eklendi !");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcı eklerken hata meydana geldi. Lütfen tekrar dene.");
            }
        }, Volley.newRequestQueue(this));
    }

    private boolean checkFields(String userName, String email, String password, String nameSurname, String phone, String companyName) {
        return !userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nameSurname.isEmpty() && !phone.isEmpty() && !companyName.isEmpty();
    }
}
