package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.Component.Button.ButtonManager;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMaintenanceService;
import me.t3sl4.ondergrup.Util.Util;

public class CreateMaintenance extends AppCompatActivity {
    public User receivedUser;

    private ArrayList<EditText> editTextList = new ArrayList<>();

    private Dialog uyariDiyalog;

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

    private String maintenance1_1="1", maintenance1_2="1", maintenance1_3="1", maintenance1_4="1";
    private String maintenance2_1="1", maintenance2_2="1", maintenance2_3="1", maintenance2_4="1";
    private String maintenance3_1="1", maintenance3_2="1", maintenance3_3="1", maintenance3_4="1", maintenance3_5="1", maintenance3_6="1";
    private String maintenance4_1="1", maintenance4_2="1", maintenance4_3="1", maintenance4_4="1", maintenance4_5="1", maintenance4_6="1";
    private String maintenance5_1="1", maintenance5_2="1", maintenance5_3="1", maintenance5_4="1", maintenance5_5="1", maintenance5_6="1";
    private String maintenance6_1="1", maintenance6_2="1", maintenance6_3="1";
    private String maintenance7_1="1", maintenance7_2="1";
    private String maintenance8_1="1", maintenance8_2="1", maintenance8_3="1";

    private String note1="", note2="", note3="", note4="", note5="", note6="", note7="", note8="", note9="", note10="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog_create);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        machineID = intent.getStringExtra("currentMahine");

        initializeComponents();

        buttonClickListeners();

        fonksiyonlarVeKontrolProcess();
    }

    private void initializeComponents() {
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
    }

    private void buttonClickListeners() {
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

        createMaintenanceLog.setOnClickListener(v -> {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            View view = getCurrentFocus();
            if (view == null) {
                view = new View(this);
            }

            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            if(checkIfAnyMaintenanceVariableIsNull()) {
                try {
                    createMaintenanceRequest();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            } else {
                Util.showErrorPopup(uyariDiyalog, "Bakım kaydı oluşturulamadı. \nLütfen bilgileri kontrol edip tekrar deneyin.");
            }
        });
    }

    private void fonksiyonlarVeKontrolProcess() {
        enableSection(1);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance1_1), ""},
                {this.getResources().getString(R.string.maintenance1_2), ""},
                {this.getResources().getString(R.string.maintenance1_3), ""},
                {this.getResources().getString(R.string.maintenance1_4), ""}
        };

        fillTableWithData(tableLayout, data, 1);
    }

    private void platformMontajProcess() {
        enableSection(2);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance2_1), ""},
                {this.getResources().getString(R.string.maintenance2_2), ""},
                {this.getResources().getString(R.string.maintenance2_3), ""},
                {this.getResources().getString(R.string.maintenance2_4), ""}
        };

        fillTableWithData(tableLayout, data, 2);
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

        fillTableWithData(tableLayout, data, 3);
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

        fillTableWithData(tableLayout, data, 4);
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

        fillTableWithData(tableLayout, data, 5);
    }

    private void elektrikProcess() {
        enableSection(6);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance6_1), " "},
                {this.getResources().getString(R.string.maintenance6_2), " "},
                {this.getResources().getString(R.string.maintenance6_3), " "}
        };
        fillTableWithData(tableLayout, data, 6);
    }

    private void kilavuzVeEtiketProcess() {
        enableSection(7);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance7_1), " "},
                {this.getResources().getString(R.string.maintenance7_2), " "}
        };

        fillTableWithData(tableLayout, data, 7);
    }

    private void saseProcess() {
        enableSection(8);
        String[][] data = {
                {this.getResources().getString(R.string.maintenance8_1), " "},
                {this.getResources().getString(R.string.maintenance8_2), " "},
                {this.getResources().getString(R.string.maintenance8_3), " "}
        };

        fillTableWithData(tableLayout, data, 8);
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

        fillTableWithData(tableLayout, data, 9);
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

    private void fillTableWithData(TableLayout tableLayout, String[][] data, int state) {
        for (int i = 0; i < data.length; i++) {
            TableRow tableRow = new TableRow(this);
            TextView textView = new TextView(this);
            textView.setText(data[i][0]);
            textView.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
            tableRow.addView(textView);

            int tempIndex = i;
            if (state == 9) {
                EditText editText = new EditText(this);
                editText.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));

                editText.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        String text = editText.getText().toString();

                        switch (tempIndex) {
                            case 0:
                                note1 = text;
                                break;
                            case 1:
                                note2 = text;
                                break;
                            case 2:
                                note3 = text;
                                break;
                            case 3:
                                note4 = text;
                                break;
                            case 4:
                                note5 = text;
                                break;
                            case 5:
                                note6 = text;
                                break;
                            case 6:
                                note7 = text;
                                break;
                            case 7:
                                note8 = text;
                                break;
                            case 8:
                                note9 = text;
                                break;
                            case 9:
                                note10 = text;
                                break;
                            default:
                                Log.d("EditTextLog", "Unknown index: " + tempIndex);
                                break;
                        }
                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        Log.d("EditTextLog", "Metin: " + s.toString());
                    }
                });
                tableRow.addView(editText);
            } else {
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
                        updateMaintenanceVariables(state, index, options[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                tableRow.addView(spinner);
            }
            tableLayout.addView(tableRow);
        }
    }

    private void updateMaintenanceVariables(int state, int index, String selectedValue) {
        switch (state) {
            case 1:
                switch(index) {
                    case 0:
                        maintenance1_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance1_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance1_3 = maintenanceFromString(selectedValue);
                        break;
                    case 3:
                        maintenance1_4 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 2:
                switch(index) {
                    case 0:
                        maintenance2_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance2_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance2_3 = maintenanceFromString(selectedValue);
                        break;
                    case 3:
                        maintenance2_4 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 3:
                switch(index) {
                    case 0:
                        maintenance3_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance3_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance3_3 = maintenanceFromString(selectedValue);
                        break;
                    case 3:
                        maintenance3_4 = maintenanceFromString(selectedValue);
                        break;
                    case 4:
                        maintenance3_5 = maintenanceFromString(selectedValue);
                        break;
                    case 5:
                        maintenance3_6 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 4:
                switch(index) {
                    case 0:
                        maintenance4_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance4_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance4_3 = maintenanceFromString(selectedValue);
                        break;
                    case 3:
                        maintenance4_4 = maintenanceFromString(selectedValue);
                        break;
                    case 4:
                        maintenance4_5 = maintenanceFromString(selectedValue);
                        break;
                    case 5:
                        maintenance4_6 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 5:
                switch(index) {
                    case 0:
                        maintenance5_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance5_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance5_3 = maintenanceFromString(selectedValue);
                        break;
                    case 3:
                        maintenance5_4 = maintenanceFromString(selectedValue);
                        break;
                    case 4:
                        maintenance5_5 = maintenanceFromString(selectedValue);
                        break;
                    case 5:
                        maintenance5_6 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 6:
                switch(index) {
                    case 0:
                        maintenance6_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance6_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance6_3 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 7:
                switch(index) {
                    case 0:
                        maintenance7_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance7_2 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            case 8:
                switch(index) {
                    case 0:
                        maintenance8_1 = maintenanceFromString(selectedValue);
                        break;
                    case 1:
                        maintenance8_2 = maintenanceFromString(selectedValue);
                        break;
                    case 2:
                        maintenance8_3 = maintenanceFromString(selectedValue);
                        break;
                    default:
                        break;
                }
                break;
            default:
                break;
        }
    }

    private String maintenanceFromString(String maintenanceCode) {
        String maintenanceStatus = "";
        String tamamText = CreateMaintenance.this.getResources().getString(R.string.maintenance_tamam);
        String hayirText = CreateMaintenance.this.getResources().getString(R.string.maintenance_hayir);
        String duzeltText = CreateMaintenance.this.getResources().getString(R.string.maintenance_duzeltme);
        String yokText = CreateMaintenance.this.getResources().getString(R.string.maintenance_yok);

        if(maintenanceCode.equals(tamamText)) {
            maintenanceStatus = "1";
        } else if(maintenanceCode.equals(hayirText)) {
            maintenanceStatus = "2";
        } else if(maintenanceCode.equals(duzeltText)) {
            maintenanceStatus = "3";
        } else if(maintenanceCode.equals(yokText)) {
            maintenanceStatus = "4";
        }

        return maintenanceStatus;
    }

    private void createMaintenanceRequest() throws JSONException {
        OPMaintenanceService.createMaintenance(this, getMaintenanceData(), () -> {
            Util.showSuccessPopup(uyariDiyalog, "Bakım kaydı oluşturuldu.");
            new Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 1000);
        });
    }

    private String getMaintenanceData() throws JSONException {
        JSONObject requestBody = new JSONObject();

        requestBody.put("machineID", machineID);
        requestBody.put("technicianID", UserDataService.getUserID(this));
        requestBody.put("kontrol11", maintenance1_1);
        requestBody.put("kontrol12", maintenance1_2);
        requestBody.put("kontrol13", maintenance1_3);
        requestBody.put("kontrol14", maintenance1_4);
        requestBody.put("kontrol21", maintenance2_1);
        requestBody.put("kontrol22", maintenance2_2);
        requestBody.put("kontrol23", maintenance2_3);
        requestBody.put("kontrol24", maintenance2_4);
        requestBody.put("kontrol31", maintenance3_1);
        requestBody.put("kontrol32", maintenance3_2);
        requestBody.put("kontrol33", maintenance3_3);
        requestBody.put("kontrol34", maintenance3_4);
        requestBody.put("kontrol35", maintenance3_5);
        requestBody.put("kontrol36", maintenance3_6);
        requestBody.put("kontrol41", maintenance4_1);
        requestBody.put("kontrol42", maintenance4_2);
        requestBody.put("kontrol43", maintenance4_3);
        requestBody.put("kontrol44", maintenance4_4);
        requestBody.put("kontrol45", maintenance4_5);
        requestBody.put("kontrol46", maintenance4_6);
        requestBody.put("kontrol51", maintenance5_1);
        requestBody.put("kontrol52", maintenance5_2);
        requestBody.put("kontrol53", maintenance5_3);
        requestBody.put("kontrol54", maintenance5_4);
        requestBody.put("kontrol55", maintenance5_5);
        requestBody.put("kontrol56", maintenance5_6);
        requestBody.put("kontrol61", maintenance6_1);
        requestBody.put("kontrol62", maintenance6_2);
        requestBody.put("kontrol63", maintenance6_3);
        requestBody.put("kontrol71", maintenance7_1);
        requestBody.put("kontrol72", maintenance7_2);
        requestBody.put("kontrol81", maintenance8_1);
        requestBody.put("kontrol82", maintenance8_2);
        requestBody.put("kontrol83", maintenance8_3);
        requestBody.put("kontrol91", note1);
        requestBody.put("kontrol92", note2);
        requestBody.put("kontrol93", note3);
        requestBody.put("kontrol94", note4);
        requestBody.put("kontrol95", note5);
        requestBody.put("kontrol96", note6);
        requestBody.put("kontrol97", note7);
        requestBody.put("kontrol98", note8);
        requestBody.put("kontrol99", note9);
        requestBody.put("kontrol910", note10);

        return requestBody.toString();
    }

    public boolean checkIfAnyMaintenanceVariableIsNull() {
        return maintenance1_1 != null && maintenance1_2 != null && maintenance1_3 != null && maintenance1_4 != null &&
                maintenance2_1 != null && maintenance2_2 != null && maintenance2_3 != null && maintenance2_4 != null &&
                maintenance3_1 != null && maintenance3_2 != null && maintenance3_3 != null && maintenance3_4 != null && maintenance3_5 != null && maintenance3_6 != null &&
                maintenance4_1 != null && maintenance4_2 != null && maintenance4_3 != null && maintenance4_4 != null && maintenance4_5 != null && maintenance4_6 != null &&
                maintenance5_1 != null && maintenance5_2 != null && maintenance5_3 != null && maintenance5_4 != null && maintenance5_5 != null && maintenance5_6 != null &&
                maintenance6_1 != null && maintenance6_2 != null && maintenance6_3 != null &&
                maintenance7_1 != null && maintenance7_2 != null &&
                maintenance8_1 != null && maintenance8_2 != null && maintenance8_3 != null;
    }
}
