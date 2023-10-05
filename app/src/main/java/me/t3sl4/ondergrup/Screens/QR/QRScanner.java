package me.t3sl4.ondergrup.Screens.QR;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.Screens.Support.SupportScreen;

public class QRScanner extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1001;
    private ZXingScannerView scannerView;

    private String fromScreen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        Intent intent = getIntent();
        fromScreen = intent.getStringExtra("fromScreen");

        Log.e("fromUs", "1" + fromScreen);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void handleResult(Result rawResult) {
        String scannedResult = rawResult.getText();

        SupportScreen.scannedQRCode = scannedResult;

        Toast.makeText(this, "Taranan QR Kod: " + scannedResult, Toast.LENGTH_SHORT).show();

        finish();
        if (scannedResult != null && SupportScreen.scannedQRCodeEditText != null) {
            if(fromScreen.equals("SubUser")) {
                Log.e("fromUs", "2a" + fromScreen);
                SubUserScreen.scannedQRCodeEditText.setText(scannedResult);
            } else if(fromScreen.equals("Support")) {
                SupportScreen.scannedQRCodeEditText.setText(scannedResult);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                scannerView.setResultHandler(this);
                scannerView.startCamera();
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
