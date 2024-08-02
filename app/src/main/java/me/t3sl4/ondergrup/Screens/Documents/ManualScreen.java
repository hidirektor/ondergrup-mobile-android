package me.t3sl4.ondergrup.Screens.Documents;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class ManualScreen extends AppCompatActivity {
    public Util util;

    private WebView dataWebView;
    private ImageView closeButton;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents_manual);

        util = new Util(getApplicationContext());

        dataWebView = findViewById(R.id.webview);
        closeButton = findViewById(R.id.closeImageView);

        dataWebView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = dataWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        dataWebView.getSettings().setBuiltInZoomControls(true);
        dataWebView.getSettings().setDisplayZoomControls(false);

        //dataWebView.loadUrl("https://docs.google.com/gview?embedded=true&url=" + util.manualPDFUrlPrefix);

        closeButton.setOnClickListener(v -> finish());
    }
}
