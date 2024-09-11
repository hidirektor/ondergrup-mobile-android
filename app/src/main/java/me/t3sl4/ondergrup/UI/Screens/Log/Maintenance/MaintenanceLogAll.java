package me.t3sl4.ondergrup.UI.Screens.Log.Maintenance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Adapter.MaintenanceAllAdapter;
import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Machine.MachineService;
import me.t3sl4.ondergrup.Util.Util;

public class MaintenanceLogAll extends AppCompatActivity {
    private ImageView backToMain;

    private ListView machineMaintenances;
    private MaintenanceAllAdapter machineMaintenanceAdapter;
    private ArrayList<Maintenance> machineMaintenanceList;

    private Dialog uyariDiyalog;

    public User receivedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog_all);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();

        buttonClickListeners();
    }

    private void initializeComponents() {
        backToMain = findViewById(R.id.backToMain);

        machineMaintenances = findViewById(R.id.machineMaintenancesAll);
        machineMaintenanceList = getMachineMaintenancesList();
    }

    private void buttonClickListeners() {
        machineMaintenances.setOnItemClickListener((parent, view, position, id) -> {
            Maintenance selectedMaintenance = machineMaintenanceList.get(position);

            Intent machineIntent = new Intent(MaintenanceLogAll.this, MaintenanceSingle.class);
            machineIntent.putExtra("currentMaintenance", selectedMaintenance);
            startActivity(machineIntent);
        });

        backToMain.setOnClickListener(v -> {
            finish();
        });
    }

    private ArrayList<Maintenance> getMachineMaintenancesList() {
        ArrayList<Maintenance> machineMaintenancesTemp = new ArrayList<>();

        MachineService.getMaintenancesAll(this, UserDataService.getUserID(this), machineMaintenancesTemp, () -> {
            updateListView(machineMaintenancesTemp);
        }, () -> {
            String hataMesaj = this.getResources().getString(R.string.bakimyok_all);
            Util.showErrorPopup(uyariDiyalog, hataMesaj);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                uyariDiyalog.dismiss();
                finish();
            }, 1000);
        });

        return machineMaintenancesTemp;
    }

    private void updateListView(ArrayList<Maintenance> machineMaintenancesTemp) {
        machineMaintenanceList = machineMaintenancesTemp;
        machineMaintenanceAdapter = new MaintenanceAllAdapter(this, machineMaintenanceList);
        machineMaintenances.setAdapter(machineMaintenanceAdapter);
    }
}
