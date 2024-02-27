package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.SplashActivity;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.Util;

public class CreateMaintenance extends AppCompatActivity {
    private Map<Integer, Integer> spinnerSelections = new HashMap<>();

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

    private String machineID;

    private Button createMaintenanceLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog_create);

        Intent intent = getIntent();
        machineID = intent.getStringExtra("currentMahine");

        backToMachine = findViewById(R.id.backButton);
        createMaintenanceLog = findViewById(R.id.createMaintenanceLog);

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

        backToMachine.setOnClickListener(v -> finish());

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
                {this.getResources().getString(R.string.maintenance1_1), ""},
                {this.getResources().getString(R.string.maintenance1_2), ""},
                {this.getResources().getString(R.string.maintenance1_3), ""},
                {this.getResources().getString(R.string.maintenance1_4), ""}
        };

        fillTableWithData(tableLayout, data);
    }

    private void platformMontajProcess() {
        enableSection(2);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance2_1), ""},
                {this.getResources().getString(R.string.maintenance2_2), ""},
                {this.getResources().getString(R.string.maintenance2_3), ""},
                {this.getResources().getString(R.string.maintenance2_4), ""}
        };

        fillTableWithData(tableLayout, data);
    }

    private void makaslarProcess() {
        enableSection(3);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance3_1), " "},
                {this.getResources().getString(R.string.maintenance3_2), " "},
                {this.getResources().getString(R.string.maintenance3_3), " "},
                {this.getResources().getString(R.string.maintenance3_4), " "},
                {this.getResources().getString(R.string.maintenance3_5), " "},
                {this.getResources().getString(R.string.maintenance3_6), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void genelProcess() {
        enableSection(4);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance4_1), " "},
                {this.getResources().getString(R.string.maintenance4_2), " "},
                {this.getResources().getString(R.string.maintenance4_3), " "},
                {this.getResources().getString(R.string.maintenance4_4), " "},
                {this.getResources().getString(R.string.maintenance4_5), " "},
                {this.getResources().getString(R.string.maintenance4_6), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void hidrolikProcess() {
        enableSection(5);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance5_1), " "},
                {this.getResources().getString(R.string.maintenance5_2), " "},
                {this.getResources().getString(R.string.maintenance5_3), " "},
                {this.getResources().getString(R.string.maintenance5_4), " "},
                {this.getResources().getString(R.string.maintenance5_5), " "},
                {this.getResources().getString(R.string.maintenance5_6), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void elektrikProcess() {
        enableSection(6);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance6_1), " "},
                {this.getResources().getString(R.string.maintenance6_2), " "},
                {this.getResources().getString(R.string.maintenance6_3), " "}
        };
        fillTableWithData(tableLayout, data);
    }

    private void kilavuzVeEtiketProcess() {
        enableSection(7);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance7_1), " "},
                {this.getResources().getString(R.string.maintenance7_2), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void saseProcess() {
        enableSection(8);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance8_1), " "},
                {this.getResources().getString(R.string.maintenance8_2), " "},
                {this.getResources().getString(R.string.maintenance8_3), " "}
        };

        fillTableWithData(tableLayout, data);
    }

    private void aciklamaNotProcess() {
        enableSection(9);
        String[][] data = {
                {this.getResources().getString(R.string.note) + " 1", " "},
                {this.getResources().getString(R.string.note) + " 2", " "},
                {this.getResources().getString(R.string.note) + " 3", " "},
                {this.getResources().getString(R.string.note) + " 4", " "},
                {this.getResources().getString(R.string.note) + " 5", " "},
                {this.getResources().getString(R.string.note) + " 6", " "},
                {this.getResources().getString(R.string.note) + " 7", " "},
                {this.getResources().getString(R.string.note) + " 8", " "},
                {this.getResources().getString(R.string.note) + " 9", " "},
                {this.getResources().getString(R.string.note) + " 10", " "}
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
        for (int i = 0; i < data.length; i++) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(data[i][0]);
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(textView);

            Spinner spinner = new Spinner(this);

            String[] options = {
                    getResources().getString(R.string.maintenance_tamam),
                    getResources().getString(R.string.maintenance_hayir),
                    getResources().getString(R.string.maintenance_duzeltme),
                    getResources().getString(R.string.maintenance_yok)
            };

            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, options);
            spinner.setAdapter(adapter);
            spinner.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

            final int index = i;
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    spinnerSelections.put(index, position + 1);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            tableRow.addView(spinner);
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
