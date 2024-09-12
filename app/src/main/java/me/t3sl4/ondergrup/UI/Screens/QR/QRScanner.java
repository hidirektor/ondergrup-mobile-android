package me.t3sl4.ondergrup.UI.Screens.QR;

import android.os.Bundle;

import androidx.fragment.app.FragmentActivity;

import me.t3sl4.ondergrup.R;

public class QRScanner extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_barcode_scanner);

        ScanFragment scanFragment = new ScanFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, scanFragment)
                .commit();
    }
}