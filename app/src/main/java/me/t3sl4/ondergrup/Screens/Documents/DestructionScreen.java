package me.t3sl4.ondergrup.Screens.Documents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;

public class DestructionScreen extends AppCompatActivity {

    private WebView destructionWebView;
    private ImageView closeButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_destruction);

        destructionWebView = findViewById(R.id.webview);
        closeButton = findViewById(R.id.closeImageView);

        destructionWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = destructionWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        destructionWebView.loadUrl("https://www.ondergrup.com/veri-saklama-ve-imha-politikasi/");

        closeButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (destructionWebView.canGoBack()) {
            destructionWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
