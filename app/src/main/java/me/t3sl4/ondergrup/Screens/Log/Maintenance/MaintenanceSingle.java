package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;

public class MaintenanceSingle extends AppCompatActivity {

    private ImageView backToMachine;

    private Button fonksiyonlarVeKontrol;
    private Button platformMontaj;
    private Button makaslar;

    private Button genel;
    private Button hidrolik;
    private Button elektrik;

    private Button kilavuzVeEtiket;
    private Button sase;
    private Button aciklamaNot;

    private Maintenance currentMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog_single);

        Intent intent = getIntent();
        currentMaintenance = intent.getParcelableExtra("currentMaintenance");

        backToMachine = findViewById(R.id.backButton);

        fonksiyonlarVeKontrol = findViewById(R.id.fonksiyonlarVeKontrol);
        platformMontaj = findViewById(R.id.platformMontaj);
        makaslar = findViewById(R.id.makaslar);

        genel = findViewById(R.id.genel);
        hidrolik = findViewById(R.id.hidrolik);
        elektrik = findViewById(R.id.elektrik);

        kilavuzVeEtiket = findViewById(R.id.kilavuzVeEtiket);
        sase = findViewById(R.id.sase);
        aciklamaNot = findViewById(R.id.aciklamaNot);

        backToMachine.setOnClickListener(v -> {
            finish();
        });

        fonksiyonlarVeKontrol.setOnClickListener(v -> fonksiyonlarVeKontrolProcess());
        platformMontaj.setOnClickListener(v -> platformMontajProcess());
        makaslar.setOnClickListener(v -> makaslarProcess());

        genel.setOnClickListener(v -> genelProcess());
        hidrolik.setOnClickListener(v -> hidrolikProcess());
        elektrik.setOnClickListener(v -> elektrikProcess());

        kilavuzVeEtiket.setOnClickListener(v -> kilavuzVeEtiketProcess());
        sase.setOnClickListener(v -> saseProcess());
        aciklamaNot.setOnClickListener(v -> aciklamaNotProcess());
    }

    private void fonksiyonlarVeKontrolProcess() {
        enableSection(1);
    }

    private void platformMontajProcess() {
        enableSection(2);
    }

    private void makaslarProcess() {
        enableSection(3);
    }

    private void genelProcess() {
        enableSection(4);
    }

    private void hidrolikProcess() {
        enableSection(5);
    }

    private void elektrikProcess() {
        enableSection(6);
    }

    private void kilavuzVeEtiketProcess() {
        enableSection(7);
    }

    private void saseProcess() {
        enableSection(8);
    }

    private void aciklamaNotProcess() {
        enableSection(9);
    }

    private void enableSection(int type) {
        ButtonManager.resetAll(this, fonksiyonlarVeKontrol, platformMontaj, makaslar, genel, hidrolik, elektrik, kilavuzVeEtiket, sase, aciklamaNot);

        if(type == 1) {
            fonksiyonlarVeKontrol.setBackgroundResource(R.drawable.button_filter_start_dark);
            fonksiyonlarVeKontrol.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 2) {
            platformMontaj.setBackgroundResource(R.drawable.button_filter_mid_dark);
            platformMontaj.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 3) {
            makaslar.setBackgroundResource(R.drawable.button_filter_end_dark);
            makaslar.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 4) {
            genel.setBackgroundResource(R.drawable.button_filter_start_dark);
            genel.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 5) {
            hidrolik.setBackgroundResource(R.drawable.button_filter_mid_dark);
            hidrolik.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 6) {
            elektrik.setBackgroundResource(R.drawable.button_filter_end_dark);
            elektrik.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 7) {
            kilavuzVeEtiket.setBackgroundResource(R.drawable.button_filter_start_dark);
            kilavuzVeEtiket.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 8) {
            sase.setBackgroundResource(R.drawable.button_filter_mid_dark);
            sase.setTextColor(ContextCompat.getColor(this, R.color.white));
        } else if(type == 9) {
            aciklamaNot.setBackgroundResource(R.drawable.button_filter_end_dark);
            aciklamaNot.setTextColor(ContextCompat.getColor(this, R.color.white));
        }
    }
}
