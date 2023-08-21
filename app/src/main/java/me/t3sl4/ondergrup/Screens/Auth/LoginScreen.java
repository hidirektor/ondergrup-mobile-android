package me.t3sl4.ondergrup.Screens.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONException;
import org.json.JSONObject;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Util.HTTPRequest;

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

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("username", username);
            requestBody.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        HTTPRequest.post("http://85.95.231.92:3000/api/login", requestBody, response -> {
            try {
                JSONObject jsonResponse = new JSONObject(response);
                boolean success = jsonResponse.getBoolean("Giriş başarılı");

                if (success) {
                    Intent intent = new Intent(LoginScreen.this, DashboardUserScreen.class);
                    startActivity(intent);
                } else {
                    // Handle unsuccessful login here
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        });
    }
}
