package me.t3sl4.ondergrup.Screens.Documents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;

public class KVKKScreen extends AppCompatActivity {

    private WebView dataWebView;
    private ImageView closeButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_kvkk);

        dataWebView = findViewById(R.id.webview);
        closeButton = findViewById(R.id.closeImageView);

        dataWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = dataWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        dataWebView.loadUrl("https://www.ondergrup.com/kvkk-politikasi/");

        closeButton.setOnClickListener(v -> {
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        if (dataWebView.canGoBack()) {
            dataWebView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
