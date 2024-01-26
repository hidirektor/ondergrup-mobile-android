package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Adapter.MaintenanceAdapter;
import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Model.MachineError.MachineError;
import me.t3sl4.ondergrup.Model.MachineError.Adapter.MachineErrorAdapter;
import me.t3sl4.ondergrup.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.Screens.Machine.MachineScreen;
import me.t3sl4.ondergrup.SplashActivity;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.Util;

public class MaintenanceLog extends AppCompatActivity {
    public Util util;

    private LinearLayout backToMachine;

    private ListView machineMaintenances;
    private MaintenanceAdapter machineMaintenanceAdapter;
    private ArrayList<Maintenance> machineMaintenanceList;

    private Dialog uyariDiyalog;

    String currentMachineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog);

        util = new Util(this);
        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        if(intent != null) {
            currentMachineID = intent.getStringExtra("currentmachine");
        }

        backToMachine = findViewById(R.id.backToMachine);

        machineMaintenances = findViewById(R.id.machineMaintenances);
        machineMaintenanceList = getMachineErrorList();

        machineMaintenances.setOnItemClickListener((parent, view, position, id) -> {
            Maintenance selectedMaintenance = machineMaintenanceList.get(position);

            Intent machineIntent = new Intent(MaintenanceLog.this, MaintenanceSingle.class);
            machineIntent.putExtra("currentMaintenance", selectedMaintenance);
            startActivity(machineIntent);
        });

        backToMachine.setOnClickListener(v -> {
            finish();
        });
    }

    private ArrayList<Maintenance> getMachineErrorList() {
        ArrayList<Maintenance> machineErrorsTemp = new ArrayList<>();
        String reqURL = util.BASE_URL + util.getMachineMaintenanceURL;
        String jsonErrorBody = "{\"machineID\": \"" + currentMachineID + "\"}";

        Log.d("ErrorLog-URL", reqURL);
        Log.d("ErrorLog-Body", jsonErrorBody);

        HTTP.sendRequest(reqURL, jsonErrorBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray machineArray = response.getJSONArray("data");
                    for (int i = 0; i < machineArray.length(); i++) {
                        JSONObject maintenanceInfoObj = machineArray.getJSONObject(i);
                        String maintenanceID = maintenanceInfoObj.getString("maintenanceID");
                        String machineID = maintenanceInfoObj.getString("machineID");
                        String incharge = maintenanceInfoObj.getString("technician");
                        String maintenanceDate = maintenanceInfoObj.getString("maintenanceDate");

                        Maintenance selectedMaintenance = new Maintenance(maintenanceID, machineID, incharge, maintenanceDate);
                        selectedMaintenance.setKontrol11(maintenanceInfoObj.getString("kontrol1-1"));
                        selectedMaintenance.setKontrol12(maintenanceInfoObj.getString("kontrol1-2"));
                        selectedMaintenance.setKontrol13(maintenanceInfoObj.getString("kontrol1-3"));
                        selectedMaintenance.setKontrol14(maintenanceInfoObj.getString("kontrol1-4"));
                        selectedMaintenance.setKontrol21(maintenanceInfoObj.getString("kontrol2-1"));
                        selectedMaintenance.setKontrol22(maintenanceInfoObj.getString("kontrol2-2"));
                        selectedMaintenance.setKontrol23(maintenanceInfoObj.getString("kontrol2-3"));
                        selectedMaintenance.setKontrol24(maintenanceInfoObj.getString("kontrol2-4"));
                        selectedMaintenance.setKontrol31(maintenanceInfoObj.getString("kontrol3-1"));
                        selectedMaintenance.setKontrol32(maintenanceInfoObj.getString("kontrol3-2"));
                        selectedMaintenance.setKontrol33(maintenanceInfoObj.getString("kontrol3-3"));
                        selectedMaintenance.setKontrol34(maintenanceInfoObj.getString("kontrol3-4"));
                        selectedMaintenance.setKontrol35(maintenanceInfoObj.getString("kontrol3-5"));
                        selectedMaintenance.setKontrol36(maintenanceInfoObj.getString("kontrol3-6"));
                        selectedMaintenance.setKontrol41(maintenanceInfoObj.getString("kontrol4-1"));
                        selectedMaintenance.setKontrol42(maintenanceInfoObj.getString("kontrol4-2"));
                        selectedMaintenance.setKontrol43(maintenanceInfoObj.getString("kontrol4-3"));
                        selectedMaintenance.setKontrol44(maintenanceInfoObj.getString("kontrol4-4"));
                        selectedMaintenance.setKontrol45(maintenanceInfoObj.getString("kontrol4-5"));
                        selectedMaintenance.setKontrol46(maintenanceInfoObj.getString("kontrol4-6"));
                        selectedMaintenance.setKontrol51(maintenanceInfoObj.getString("kontrol5-1"));
                        selectedMaintenance.setKontrol52(maintenanceInfoObj.getString("kontrol5-2"));
                        selectedMaintenance.setKontrol53(maintenanceInfoObj.getString("kontrol5-3"));
                        selectedMaintenance.setKontrol54(maintenanceInfoObj.getString("kontrol5-4"));
                        selectedMaintenance.setKontrol55(maintenanceInfoObj.getString("kontrol5-5"));
                        selectedMaintenance.setKontrol56(maintenanceInfoObj.getString("kontrol5-6"));
                        selectedMaintenance.setKontrol61(maintenanceInfoObj.getString("kontrol6-1"));
                        selectedMaintenance.setKontrol62(maintenanceInfoObj.getString("kontrol6-2"));
                        selectedMaintenance.setKontrol63(maintenanceInfoObj.getString("kontrol6-3"));
                        selectedMaintenance.setKontrol71(maintenanceInfoObj.getString("kontrol7-1"));
                        selectedMaintenance.setKontrol72(maintenanceInfoObj.getString("kontrol7-2"));
                        selectedMaintenance.setKontrol81(maintenanceInfoObj.getString("kontrol8-1"));
                        selectedMaintenance.setKontrol82(maintenanceInfoObj.getString("kontrol8-2"));
                        selectedMaintenance.setKontrol83(maintenanceInfoObj.getString("kontrol8-3"));
                        selectedMaintenance.setKontrol91(maintenanceInfoObj.getString("kontrol9-1"));
                        selectedMaintenance.setKontrol92(maintenanceInfoObj.getString("kontrol9-2"));
                        selectedMaintenance.setKontrol93(maintenanceInfoObj.getString("kontrol9-3"));
                        selectedMaintenance.setKontrol94(maintenanceInfoObj.getString("kontrol9-4"));
                        selectedMaintenance.setKontrol95(maintenanceInfoObj.getString("kontrol9-5"));
                        selectedMaintenance.setKontrol96(maintenanceInfoObj.getString("kontrol9-6"));
                        selectedMaintenance.setKontrol97(maintenanceInfoObj.getString("kontrol9-7"));
                        selectedMaintenance.setKontrol98(maintenanceInfoObj.getString("kontrol9-8"));
                        selectedMaintenance.setKontrol99(maintenanceInfoObj.getString("kontrol9-9"));
                        selectedMaintenance.setKontrol910(maintenanceInfoObj.getString("kontrol9-10"));
                        machineErrorsTemp.add(selectedMaintenance);
                    }

                    updateListView(machineErrorsTemp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                String hatasizMesaj = MaintenanceLog.this.getResources().getString(R.string.hatayok);
                util.showSuccessPopup(uyariDiyalog, hatasizMesaj);
            }
        }, Volley.newRequestQueue(this));
        return machineErrorsTemp;
    }

    private void updateListView(ArrayList<Maintenance> machineMaintenancesTemp) {
        machineMaintenanceList = machineMaintenancesTemp;
        machineMaintenanceAdapter = new MaintenanceAdapter(this, machineMaintenanceList);
        machineMaintenances.setAdapter(machineMaintenanceAdapter);
    }

    private void showErrorDetail(Dialog diyalog, String errorDetail) {
        diyalog.setContentView(R.layout.activity_popup_errordetail);

        int errorCode = Integer.parseInt(errorDetail);

        ImageView errorImage = diyalog.findViewById(R.id.errorImage);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TableLayout tableLayout = diyalog.findViewById(R.id.tableLayout);
        TextView errorName = diyalog.findViewById(R.id.errorName);

        Drawable drawable = null;
        String errorDesc = "";
        String[][] data = {
                {"Veri 1", "Veri 2"},
                {"Veri 3", "Veri 4"}
        };

        if(errorCode == 1) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_acilstop);
            errorDesc = this.getResources().getString(R.string.acilstop);

            String sebep1 = this.getResources().getString(R.string.error_acilstop_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_acilstop_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_acilstop_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_acilstop_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 2) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_emniyetcerceve);
            errorDesc = this.getResources().getString(R.string.emniyetcercevesi);

            String sebep1 = this.getResources().getString(R.string.error_emniyetcerceve_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_emniyetcerceve_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_emniyetcerceve_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_emniyetcerceve_cozum2);
            String sebep3 = this.getResources().getString(R.string.error_emniyetcerceve_sebep3);
            String cozum3 = this.getResources().getString(R.string.error_emniyetcerceve_cozum3);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2},
                    {sebep3, cozum3}
            };
        } else if(errorCode == 3) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_basincasiriyuk);
            errorDesc = this.getResources().getString(R.string.basincasiriyuk);

            String sebep1 = this.getResources().getString(R.string.error_basinc_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_basinc_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_basinc_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_basinc_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 4) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_kapiswitch);
            errorDesc = this.getResources().getString(R.string.kapiswitch);

            String sebep1 = this.getResources().getString(R.string.error_kapiswitch_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_kapiswitch_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_kapiswitch_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_kapiswitch_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 5) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_tablakapiswitch);
            errorDesc = this.getResources().getString(R.string.tablakapiswitch);

            String sebep1 = this.getResources().getString(R.string.error_tablakapiswitch_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_tablakapiswitch_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_tablakapiswitch_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_tablakapiswitch_cozum2);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2}
            };
        } else if(errorCode == 6) {
            drawable = ContextCompat.getDrawable(this, R.drawable.ikon_hata_maximumcalisma);
            errorDesc = this.getResources().getString(R.string.maximumcalisma);

            String sebep1 = this.getResources().getString(R.string.error_maxcalisma_sebep1);
            String cozum1 = this.getResources().getString(R.string.error_maxcalisma_cozum1);
            String sebep2 = this.getResources().getString(R.string.error_maxcalisma_sebep2);
            String cozum2 = this.getResources().getString(R.string.error_maxcalisma_cozum2);
            String sebep3 = this.getResources().getString(R.string.error_maxcalisma_sebep3);
            String cozum3 = this.getResources().getString(R.string.error_maxcalisma_cozum3);
            String sebep4 = this.getResources().getString(R.string.error_maxcalisma_sebep4);
            String cozum4 = this.getResources().getString(R.string.error_maxcalisma_cozum4);

            data = new String[][]{
                    {sebep1, cozum1},
                    {sebep2, cozum2},
                    {sebep3, cozum3},
                    {sebep4, cozum4}
            };
        }

        errorImage.setImageDrawable(drawable);
        errorName.setText(errorDesc);
        fillTableWithData(tableLayout, data);

        close.setOnClickListener(v -> diyalog.dismiss());

        Objects.requireNonNull(diyalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
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

            tableLayout.addView(tableRow);
        }
    }


}
