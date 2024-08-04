package me.t3sl4.ondergrup.Screens.Documents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;

public class PrivacyScreen extends AppCompatActivity {

    private WebView privacyWebView;
    private ImageView closeButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_privacy);

        initializeComponents();

        privacyWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = privacyWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        privacyWebView.loadUrl("https://www.ondergrup.com/gizlilik-politikasi/");

        closeButton.setOnClickListener(v -> {
            finish();
        });
    }

    private void initializeComponents() {
        privacyWebView = findViewById(R.id.webview);
        closeButton = findViewById(R.id.closeImageView);
    }

    @Override
    public void onBackPressed() {
        if (privacyWebView.canGoBack()) {
            privacyWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
