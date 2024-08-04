package me.t3sl4.ondergrup.Screens.Log.Maintenance;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Objects;

import me.t3sl4.ondergrup.Model.MachineMaintenance.Adapter.MaintenanceAdapter;
import me.t3sl4.ondergrup.Model.MachineMaintenance.Maintenance;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Machine.MachineService;

public class MaintenanceLog extends AppCompatActivity {
    public me.t3sl4.ondergrup.Model.User.User receivedUser;

    private ImageView backToMachine;

    private ListView machineMaintenances;
    private MaintenanceAdapter machineMaintenanceAdapter;
    private ArrayList<Maintenance> machineMaintenanceList;

    private Dialog uyariDiyalog;

    String currentMachineID;
    private Button createMaintenance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenancelog);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        if(intent != null) {
            currentMachineID = intent.getStringExtra("currentmachine");
            receivedUser = intent.getParcelableExtra("user");
        }

        initializeComponents();

        if(Objects.equals(receivedUser.getRole(), "NORMAL")) {
            createMaintenance.setVisibility(View.GONE);
        }

        buttonClickListeners();
    }

    private void initializeComponents() {
        backToMachine = findViewById(R.id.backToMachine);
        createMaintenance = findViewById(R.id.createMaintenanceLog);

        machineMaintenances = findViewById(R.id.machineMaintenances);
        machineMaintenanceList = getMachineMaintenanceList();
    }

    private void buttonClickListeners() {
        machineMaintenances.setOnItemClickListener((parent, view, position, id) -> {
            Maintenance selectedMaintenance = machineMaintenanceList.get(position);

            Intent maintenanceIntent = new Intent(MaintenanceLog.this, MaintenanceSingle.class);
            maintenanceIntent.putExtra("currentMaintenance", selectedMaintenance);
            startActivity(maintenanceIntent);
        });

        backToMachine.setOnClickListener(v -> finish());

        createMaintenance.setOnClickListener(v -> {
            //Bakım kaydı oluşturma
            Intent machineIntent = new Intent(MaintenanceLog.this, CreateMaintenance.class);
            machineIntent.putExtra("user", receivedUser);
            machineIntent.putExtra("currentMahine", currentMachineID);
            startActivity(machineIntent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        machineMaintenanceList = getMachineMaintenanceList();
    }

    private ArrayList<Maintenance> getMachineMaintenanceList() {
        ArrayList<Maintenance> machineMaintenancesTemp = new ArrayList<>();
        MachineService.getMaintenances(this, currentMachineID, machineMaintenancesTemp, () -> {
            updateListView(machineMaintenancesTemp);
        });
        return machineMaintenancesTemp;
    }

    private void updateListView(ArrayList<Maintenance> machineMaintenancesTemp) {
        machineMaintenanceList = machineMaintenancesTemp;
        machineMaintenanceAdapter = new MaintenanceAdapter(this, machineMaintenanceList);
        machineMaintenances.setAdapter(machineMaintenanceAdapter);
    }
}
