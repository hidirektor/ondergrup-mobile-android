package me.t3sl4.ondergrup.UI.Screens.Documents;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Web.WebViewBottomSheetFragment;

public class DocumentsScreen extends AppCompatActivity {

    private LinearLayout dataButton;
    private LinearLayout privacyButton;
    private LinearLayout imhaButton;
    private LinearLayout manualButton;

    private ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_documents);

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        dataButton = findViewById(R.id.kvkkLinearLayout);
        privacyButton = findViewById(R.id.privacyLinearLayout);
        imhaButton = findViewById(R.id.destructionLinearLayout);
        manualButton = findViewById(R.id.manualLinearLayout);
        backButton = findViewById(R.id.backButton);
    }

    private void buttonClickListeners() {
        manualButton.setOnClickListener(v -> {
            String pdfUrl = "https://hidirektor.com.tr/manual";
            WebViewBottomSheetFragment webViewBottomSheet = new WebViewBottomSheetFragment(pdfUrl);
            webViewBottomSheet.show(getSupportFragmentManager(), "WebViewBottomSheet");
        });

        dataButton.setOnClickListener(v -> {
            WebViewBottomSheetFragment webViewBottomSheet = new WebViewBottomSheetFragment("https://www.ondergrup.com/kvkk-politikasi/");
            webViewBottomSheet.show(getSupportFragmentManager(), "WebViewBottomSheet");
        });

        privacyButton.setOnClickListener(v -> {
            WebViewBottomSheetFragment webViewBottomSheet = new WebViewBottomSheetFragment("https://www.ondergrup.com/gizlilik-politikasi/");
            webViewBottomSheet.show(getSupportFragmentManager(), "WebViewBottomSheet");
        });

        imhaButton.setOnClickListener(v -> {
            WebViewBottomSheetFragment webViewBottomSheet = new WebViewBottomSheetFragment("https://www.ondergrup.com/veri-saklama-ve-imha-politikasi/");
            webViewBottomSheet.show(getSupportFragmentManager(), "WebViewBottomSheet");
        });

        backButton.setOnClickListener(v -> finish());
    }
}
