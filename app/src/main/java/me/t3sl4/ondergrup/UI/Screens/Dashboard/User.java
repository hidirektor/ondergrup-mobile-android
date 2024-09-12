package me.t3sl4.ondergrup.UI.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.BaseActivity;
import me.t3sl4.ondergrup.UI.Screens.Log.Error.ErrorLogAll;
import me.t3sl4.ondergrup.UI.Screens.Log.Maintenance.MaintenanceLogAll;
import me.t3sl4.ondergrup.UI.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.UI.Screens.Machine.MachineScreen;
import me.t3sl4.ondergrup.UI.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.UI.Screens.Settings.SettingsDashboard;
import me.t3sl4.ondergrup.UI.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.UI.Screens.SupportTicket.SupportTickets;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Machine.MachineService;
import me.t3sl4.ondergrup.Util.Util;

public class User extends BaseActivity {
    private static final int QR_REQUEST_CODE = 123;
    private TextView isimSoyisim;


    //Sub Buttons:
    private ConstraintLayout myMachineButton;
    private ConstraintLayout supportButton;
    private FloatingActionButton qrButton;
    private ConstraintLayout subUserButton;
    private ConstraintLayout settingsButton;


    //Header Buttons
    private ImageView logoutButton;
    private ConstraintLayout allErrorsButton;
    private ConstraintLayout allMaintenancesButton;

    //Machine List View Section:
    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    public me.t3sl4.ondergrup.Model.User.User receivedUser;

    private Dialog uyariDiyalog;
    private Dialog qrDiyalog;

    public static String scannedQRCode;
    public static EditText scannedQRCodeEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        uyariDiyalog = new Dialog(this);
        qrDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();

        buttonClickListeners();

        setUserInfo();
    }

    private void initializeComponents() {
        isimSoyisim = findViewById(R.id.loggedUserName);

        supportButton = findViewById(R.id.supportConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        subUserButton = findViewById(R.id.subUserConstraint);
        allErrorsButton = findViewById(R.id.allErrorsConstraint);
        allMaintenancesButton = findViewById(R.id.allMaintenancesConstraint);
        myMachineButton = findViewById(R.id.myMachine);
        qrButton = findViewById(R.id.qrConstraint);
        logoutButton = findViewById(R.id.logoutButton);

        //Machine List
        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();
    }

    private void buttonClickListeners() {
        logoutButton.setOnClickListener(v -> UserDataService.logout(this));

        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(User.this, MachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        supportButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(User.this, SupportTickets.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(User.this, SettingsDashboard.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });

        subUserButton.setOnClickListener(v -> {
            if(receivedUser.getOwnerName() != null) {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_error));
            } else {
                Intent settingsIntent = new Intent(User.this, SubUserScreen.class);
                settingsIntent.putExtra("user", receivedUser);
                startActivity(settingsIntent);
            }
        });

        allErrorsButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(User.this, ErrorLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        allMaintenancesButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(User.this, MaintenanceLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        myMachineButton.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(User.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
        });

        qrButton.setOnClickListener(v -> {
            addMachine();
        });
    }

    public void setUserInfo() {
        isimSoyisim.setText(this.getResources().getString(R.string.hello_prefix) + receivedUser.getNameSurname());
    }

    public void makineEkle(String machineType, String machineID) {
        String ownerID = UserDataService.getUserID(this);

        MachineService.addMachine(this, machineID, ownerID, () -> {
            Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.machine_add_successful));
            getMachineList();
            qrDiyalog.dismiss();
        }, () -> {
            Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.machine_add_error));
        });
    }

    private void openWifiSettings() {
        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
        startActivity(intent);
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();

        MachineService.getMachines(this, UserDataService.getUserID(this), machines, () -> {
            // onSuccess callback
            updateListView(machines);
        });

        return machines;
    }

    private void updateListView(ArrayList<Machine> machines) {
        machineList = machines;
        machineListAdapter = new MachineAdapter(this, machineList);
        machineListView.setAdapter(machineListAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addMachine() {
        qrDiyalog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        qrDiyalog.setContentView(R.layout.activity_machine_add);

        TextView makineTuru = qrDiyalog.findViewById(R.id.textViewMakineTuru);
        ImageView cancelButton = qrDiyalog.findViewById(R.id.cancelButton);
        Button addButton = qrDiyalog.findViewById(R.id.makineEkleButton);
        Spinner machineTypeSpinner = qrDiyalog.findViewById(R.id.machineTypeSpinner);
        View splitter = qrDiyalog.findViewById(R.id.view);

        makineTuru.setVisibility(View.GONE);
        machineTypeSpinner.setVisibility(View.GONE);
        splitter.setVisibility(View.GONE);

        scannedQRCodeEditText = qrDiyalog.findViewById(R.id.editTextID);
        if (scannedQRCode != null) {
            scannedQRCodeEditText.setText(scannedQRCode);
        }

        scannedQRCodeEditText.setOnTouchListener((vi, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (event.getRawX() >= (scannedQRCodeEditText.getRight() - scannedQRCodeEditText.getCompoundDrawables()[2].getBounds().width())) {
                        Intent intent = new Intent(User.this, QRScanner.class);
                        intent.putExtra("user", receivedUser);
                        qrResultLauncher.launch(intent);
                        return true;
                    }
            }
            return false;
        });

        cancelButton.setOnClickListener(view -> qrDiyalog.dismiss());

        addButton.setOnClickListener(view -> {
            Log.d("Selected Type", machineTypeSpinner.getSelectedItem().toString());
            if(scannedQRCode != null) {
                makineEkle(machineTypeSpinner.getSelectedItem().toString(), scannedQRCode);
            } else {
                makineEkle(machineTypeSpinner.getSelectedItem().toString(), scannedQRCodeEditText.getText().toString());
            }
        });

        String[] machineTypes = getResources().getStringArray(R.array.machineType);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, machineTypes);
        machineTypeSpinner.setAdapter(adapter);

        qrDiyalog.show();
        qrDiyalog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        qrDiyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        qrDiyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        qrDiyalog.getWindow().setGravity(Gravity.BOTTOM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == QR_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String scannedQRCode = data.getStringExtra("scannedQRCode");

            if (scannedQRCode != null) {
                scannedQRCodeEditText.setText(scannedQRCode);
                Toast.makeText(this, "QR Code: " + scannedQRCode, Toast.LENGTH_LONG).show();
            }
        }
    }

    private final ActivityResultLauncher<Intent> qrResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        String scannedQRCode = data.getStringExtra("scannedQRCode");
                        if (scannedQRCode != null) {
                            scannedQRCodeEditText.setText(scannedQRCode);
                            Toast.makeText(User.this, "QR Code: " + scannedQRCode, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );
}