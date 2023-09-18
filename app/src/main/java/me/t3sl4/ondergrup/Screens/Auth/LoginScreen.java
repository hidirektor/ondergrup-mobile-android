package me.t3sl4.ondergrup.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardEngineerScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardSysOpScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardTechnicianScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.HTTP.RequestURLs;

public class LoginScreen extends AppCompatActivity {

    private EditText editTextTextPersonName;
    private EditText editTextTextPasswordName;
    private ImageView loginButton;
    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPasswordName = findViewById(R.id.editTextTextPasswordName);
        loginButton = findViewById(R.id.loginButton);
        registerTextView = findViewById(R.id.registerTextView);

        loginButton.setOnClickListener(v -> sendLoginRequest());

        registerTextView.setOnClickListener(v -> {
            Intent intent = new Intent(LoginScreen.this, RegisterScreen.class);
            startActivity(intent);
        });
    }

    private void sendLoginRequest() {
        String username = editTextTextPersonName.getText().toString();
        String password = editTextTextPasswordName.getText().toString();

        String authenticationUrl = RequestURLs.BASE_URL + RequestURLs.loginURLPrefix;

        String jsonLoginBody = "{\"Username\": \"" + username + "\", \"Password\": \"" + password + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(authenticationUrl, jsonLoginBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) throws IOException {
                getUserType(username);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginScreen.this, "Kullanıcı adı veya şifre hatalı!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUserType(String username) throws IOException {
        String userTypeUrl = RequestURLs.BASE_URL + RequestURLs.profileInfoURLPrefix + ":Role";
        String jsonProfileInfoBody = "{\"Username\": \"" + username + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(userTypeUrl, jsonProfileInfoBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    String role = response.getString("Role");

                    Intent intent = null;

                    switch (role) {
                        case "NORMAL":
                            intent = new Intent(LoginScreen.this, DashboardUserScreen.class);
                            break;
                        case "TECHNICIAN":
                            intent = new Intent(LoginScreen.this, DashboardTechnicianScreen.class);
                            break;
                        case "ENGINEER":
                            intent = new Intent(LoginScreen.this, DashboardEngineerScreen.class);
                            break;
                        case "SYSOP":
                            intent = new Intent(LoginScreen.this, DashboardSysOpScreen.class);
                            break;
                        default:
                            Toast.makeText(LoginScreen.this, "Desteklenmeyen bir kullanıcı türüne sahipsin!", Toast.LENGTH_SHORT).show();
                            break;
                    }

                    if (intent != null) {
                        startActivity(intent);
                        finish();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(LoginScreen.this, "Profil bilgileri alınamadı!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
