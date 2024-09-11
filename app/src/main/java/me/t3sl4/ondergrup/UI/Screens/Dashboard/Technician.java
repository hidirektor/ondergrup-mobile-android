package me.t3sl4.ondergrup.UI.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.BaseActivity;
import me.t3sl4.ondergrup.UI.Screens.Log.Error.ErrorLogAll;
import me.t3sl4.ondergrup.UI.Screens.Log.Maintenance.MaintenanceLogAll;
import me.t3sl4.ondergrup.UI.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.UI.Screens.Machine.RestrictedMachineScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMachineService;
import me.t3sl4.ondergrup.Util.Util;

public class Technician extends BaseActivity {

    private TextView isimSoyisim;

    private ConstraintLayout subLanguage;
    private ConstraintLayout profileButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout myMachineButton;

    //Top Buttons:
    private ImageView logoutButton;
    private ConstraintLayout allMaintenancesButton;
    private ConstraintLayout allErrorsButton;


    //Machine List View Section:
    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    public me.t3sl4.ondergrup.Model.User.User receivedUser;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_technician);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();

        buttonClickListeners();

        setUserInfo();
    }

    private void initializeComponents() {
        isimSoyisim = findViewById(R.id.loggedUserName);

        subLanguage = findViewById(R.id.languageConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        myMachineButton = findViewById(R.id.myMachine);

        //Top buttons:
        logoutButton = findViewById(R.id.logoutButton);
        allErrorsButton = findViewById(R.id.allErrorsConstraint);
        allMaintenancesButton = findViewById(R.id.allMaintenancesConstraint);

        //ListView Definitians:
        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();
    }

    private void buttonClickListeners() {
        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(Technician.this, RestrictedMachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        //Top Buttons:
        logoutButton.setOnClickListener(v -> UserDataService.logout(this));

        allMaintenancesButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Technician.this, MaintenanceLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        allErrorsButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Technician.this, ErrorLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        //Bottom buttons:
        myMachineButton.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(Technician.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Technician.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        subLanguage.setOnClickListener(v -> {
            Util.changeSystemLanguage(this);
            recreate();
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(Technician.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });
    }

    public void setUserInfo() {
        isimSoyisim.setText(this.getResources().getString(R.string.hello_prefix) + receivedUser.getNameSurname());
    }

    private void openWifiSettings() {
        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
        startActivity(intent);
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();

        OPMachineService.getAllMachines(this, machines, () -> {
            // onSuccess callback
            updateListView(machines);
        }, () -> {
            //onFailure callback
        });

        return null;
    }

    private void updateListView(ArrayList<Machine> machines) {
        machineList = machines;
        machineListAdapter = new MachineAdapter(this, machineList);
        machineListView.setAdapter(machineListAdapter);
    }
}