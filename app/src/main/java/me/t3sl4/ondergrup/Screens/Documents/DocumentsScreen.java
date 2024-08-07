package me.t3sl4.ondergrup.Screens.Documents;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;

public class DocumentsScreen extends AppCompatActivity {

    private Button dataButton;
    private Button privacyButton;
    private Button imhaButton;
    private Button manualButton;

    private ImageView backButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        dataButton = findViewById(R.id.dataButton);
        privacyButton = findViewById(R.id.privacyButton);
        imhaButton = findViewById(R.id.imhaButton);
        manualButton = findViewById(R.id.manualButton);
        backButton = findViewById(R.id.backButton);
    }

    private void buttonClickListeners() {
        manualButton.setOnClickListener(v -> {
            Intent manualIntent = new Intent(DocumentsScreen.this, ManualScreen.class);
            startActivity(manualIntent);
        });

        dataButton.setOnClickListener(v -> {
            Intent dataIntent = new Intent(DocumentsScreen.this, KVKKScreen.class);
            startActivity(dataIntent);
        });

        privacyButton.setOnClickListener(v -> {
            Intent privacyIntent = new Intent(DocumentsScreen.this, PrivacyScreen.class);
            startActivity(privacyIntent);
        });

        imhaButton.setOnClickListener(v -> {
            Intent privacyIntent = new Intent(DocumentsScreen.this, DestructionScreen.class);
            startActivity(privacyIntent);
        });

        backButton.setOnClickListener(v -> finish());
    }
}
