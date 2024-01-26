package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.SplashActivity;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.Util;

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

    private TableLayout tableLayout;

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

        tableLayout = findViewById(R.id.tableLayout);

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

        fonksiyonlarVeKontrolProcess();
    }

    private void fonksiyonlarVeKontrolProcess() {
        enableSection(1);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance1_1), maintenanceFromCode(currentMaintenance.getKontrol11())},
                {this.getResources().getString(R.string.maintenance1_2), maintenanceFromCode(currentMaintenance.getKontrol12())},
                {this.getResources().getString(R.string.maintenance1_3), maintenanceFromCode(currentMaintenance.getKontrol13())},
                {this.getResources().getString(R.string.maintenance1_4), maintenanceFromCode(currentMaintenance.getKontrol14())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void platformMontajProcess() {
        enableSection(2);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance2_1), maintenanceFromCode(currentMaintenance.getKontrol21())},
                {this.getResources().getString(R.string.maintenance2_2), maintenanceFromCode(currentMaintenance.getKontrol22())},
                {this.getResources().getString(R.string.maintenance2_3), maintenanceFromCode(currentMaintenance.getKontrol23())},
                {this.getResources().getString(R.string.maintenance2_4), maintenanceFromCode(currentMaintenance.getKontrol24())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void makaslarProcess() {
        enableSection(3);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance3_1), maintenanceFromCode(currentMaintenance.getKontrol31())},
                {this.getResources().getString(R.string.maintenance3_2), maintenanceFromCode(currentMaintenance.getKontrol32())},
                {this.getResources().getString(R.string.maintenance3_3), maintenanceFromCode(currentMaintenance.getKontrol33())},
                {this.getResources().getString(R.string.maintenance3_4), maintenanceFromCode(currentMaintenance.getKontrol34())},
                {this.getResources().getString(R.string.maintenance3_5), maintenanceFromCode(currentMaintenance.getKontrol35())},
                {this.getResources().getString(R.string.maintenance3_6), maintenanceFromCode(currentMaintenance.getKontrol36())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void genelProcess() {
        enableSection(4);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance4_1), maintenanceFromCode(currentMaintenance.getKontrol41())},
                {this.getResources().getString(R.string.maintenance4_2), maintenanceFromCode(currentMaintenance.getKontrol42())},
                {this.getResources().getString(R.string.maintenance4_3), maintenanceFromCode(currentMaintenance.getKontrol43())},
                {this.getResources().getString(R.string.maintenance4_4), maintenanceFromCode(currentMaintenance.getKontrol44())},
                {this.getResources().getString(R.string.maintenance4_5), maintenanceFromCode(currentMaintenance.getKontrol45())},
                {this.getResources().getString(R.string.maintenance4_6), maintenanceFromCode(currentMaintenance.getKontrol46())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void hidrolikProcess() {
        enableSection(5);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance5_1), maintenanceFromCode(currentMaintenance.getKontrol51())},
                {this.getResources().getString(R.string.maintenance5_2), maintenanceFromCode(currentMaintenance.getKontrol52())},
                {this.getResources().getString(R.string.maintenance5_3), maintenanceFromCode(currentMaintenance.getKontrol53())},
                {this.getResources().getString(R.string.maintenance5_4), maintenanceFromCode(currentMaintenance.getKontrol54())},
                {this.getResources().getString(R.string.maintenance5_5), maintenanceFromCode(currentMaintenance.getKontrol55())},
                {this.getResources().getString(R.string.maintenance5_6), maintenanceFromCode(currentMaintenance.getKontrol56())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void elektrikProcess() {
        enableSection(6);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance6_1), maintenanceFromCode(currentMaintenance.getKontrol61())},
                {this.getResources().getString(R.string.maintenance6_2), maintenanceFromCode(currentMaintenance.getKontrol62())},
                {this.getResources().getString(R.string.maintenance6_3), maintenanceFromCode(currentMaintenance.getKontrol63())}
        };
        fillTableWithData(tableLayout, data);
    }

    private void kilavuzVeEtiketProcess() {
        enableSection(7);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance7_1), maintenanceFromCode(currentMaintenance.getKontrol71())},
                {this.getResources().getString(R.string.maintenance7_2), maintenanceFromCode(currentMaintenance.getKontrol72())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void saseProcess() {
        enableSection(8);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance8_1), maintenanceFromCode(currentMaintenance.getKontrol81())},
                {this.getResources().getString(R.string.maintenance8_2), maintenanceFromCode(currentMaintenance.getKontrol82())},
                {this.getResources().getString(R.string.maintenance8_3), maintenanceFromCode(currentMaintenance.getKontrol83())}
        };

        fillTableWithData(tableLayout, data);
    }

    private void aciklamaNotProcess() {
        enableSection(9);
        String[][] data = {
                {currentMaintenance.getKontrol91(), " "},
                {currentMaintenance.getKontrol92(), " "},
                {currentMaintenance.getKontrol93(), " "},
                {currentMaintenance.getKontrol94(), " "},
                {currentMaintenance.getKontrol95(), " "},
                {currentMaintenance.getKontrol96(), " "},
                {currentMaintenance.getKontrol97(), " "},
                {currentMaintenance.getKontrol98(), " "},
                {currentMaintenance.getKontrol99(), " "},
                {currentMaintenance.getKontrol910(), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void enableSection(int type) {
        ButtonManager.resetAll(this, fonksiyonlarVeKontrol, platformMontaj, makaslar, genel, hidrolik, elektrik, kilavuzVeEtiket, sase, aciklamaNot);
        tableLayout.removeAllViews();

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

    private void fillTableWithData(TableLayout tableLayout, String[][] data) {
        for (String[] rowData : data) {
            TableRow tableRow = new TableRow(this);

            for (String item : rowData) {
                TextView textView = new TextView(this);
                textView.setLayoutParams(new TableRow.LayoutParams(
                        0, // width
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1f // weight
                ));
                textView.setText(item);
                textView.setTypeface(ResourcesCompat.getFont(this, R.font.outfit_medium));
                textView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                textView.setTextColor(getResources().getColor(android.R.color.black));
                textView.setGravity(Gravity.CENTER);
                tableRow.addView(textView);
            }

            int paddingInDp = 10;
            float scale = getResources().getDisplayMetrics().density;
            int paddingInPixels = (int) (paddingInDp * scale + 0.5f);
            tableRow.setPadding(0, 0, 0, paddingInPixels);

            tableLayout.addView(tableRow);
        }
    }

    private String maintenanceFromCode(String maintenanceCode) {
        String maintenanceStatus = "";
        if(Objects.equals(maintenanceCode, "1")) {
            maintenanceStatus = this.getResources().getString(R.string.maintenance_tamam);
        } else if(Objects.equals(maintenanceCode, "2")) {
            maintenanceStatus = this.getResources().getString(R.string.maintenance_hayir);
        } else if(Objects.equals(maintenanceCode, "3")) {
            maintenanceStatus = this.getResources().getString(R.string.maintenance_duzeltme);
        } else {
            maintenanceStatus = this.getResources().getString(R.string.maintenance_yok);
        }

        return maintenanceStatus;
    }
}
