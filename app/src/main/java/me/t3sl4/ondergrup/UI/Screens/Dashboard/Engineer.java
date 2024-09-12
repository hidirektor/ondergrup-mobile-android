package me.t3sl4.ondergrup.UI.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
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
import me.t3sl4.ondergrup.UI.Screens.Machine.RestrictedMachineScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.UI.Screens.VersionHistory.VersionHistory;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMachineService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Machine.MachineService;
import me.t3sl4.ondergrup.Util.Util;

public class Engineer extends BaseActivity {
    private static final String TARGET_WIFI_SSID = "OnderGrup_IoT";
    private static final int QR_REQUEST_CODE = 123;

    private TextView isimSoyisim;

    private ConstraintLayout subLanguage;
    private ConstraintLayout profileButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout myMachineButton;
    private FloatingActionButton qrButton;

    //Top Buttons:
    private ImageView logoutButton;
    private ConstraintLayout allMaintenancesButton;
    private ConstraintLayout allErrorsButton;
    private ConstraintLayout versionHistoryButton;

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
        setContentView(R.layout.activity_main_engineer);

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

        subLanguage = findViewById(R.id.languageConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        myMachineButton = findViewById(R.id.myMachine);
        qrButton = findViewById(R.id.qrConstraint);

        //Top buttons:
        logoutButton = findViewById(R.id.logoutButton);
        allErrorsButton = findViewById(R.id.allErrorsConstraint);
        allMaintenancesButton = findViewById(R.id.allMaintenancesConstraint);
        versionHistoryButton = findViewById(R.id.versionHistoryConstraint);

        //ListView Definitians:
        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();
    }

    private void buttonClickListeners() {
        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(Engineer.this, RestrictedMachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        //Top Buttons:
        logoutButton.setOnClickListener(v -> UserDataService.logout(this));

        allMaintenancesButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Engineer.this, MaintenanceLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        allErrorsButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Engineer.this, ErrorLogAll.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        versionHistoryButton.setOnClickListener(v -> {
            Intent versionIntent = new Intent(Engineer.this, VersionHistory.class);
            versionIntent.putExtra("user", receivedUser);
            startActivity(versionIntent);
        });

        //Bottom buttons:
        myMachineButton.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(Engineer.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(Engineer.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        });

        qrButton.setOnClickListener(v -> addMachine());

        subLanguage.setOnClickListener(v -> {
            Util.changeSystemLanguage(this);
            recreate();
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(Engineer.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        machineList = getMachineList();
    }

    public void setUserInfo() {
        isimSoyisim.setText(this.getResources().getString(R.string.hello_prefix) + receivedUser.getNameSurname());
    }

    public void makineEkle(String machineType, String machineID) {
        MachineService.createMachine(this, UserDataService.getUserID(this), machineID, machineType, () -> {
            //onSuccess operations
            qrDiyalog.dismiss();
            getMachineList();
            Util.showSuccessPopup(uyariDiyalog, this.getResources().getString(R.string.machine_created));
        });
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();

        OPMachineService.getAllMachines(this, machines, () -> {
            // onSuccess callback
            updateListView(machines);
        }, () -> {
            //onFailure callback
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

        ImageView cancelButton = qrDiyalog.findViewById(R.id.cancelButton);
        Button addButton = qrDiyalog.findViewById(R.id.makineEkleButton);
        Spinner machineTypeSpinner = qrDiyalog.findViewById(R.id.machineTypeSpinner);

        scannedQRCodeEditText = qrDiyalog.findViewById(R.id.editTextID);
        if (scannedQRCode != null) {
            scannedQRCodeEditText.setText(scannedQRCode);
        }

        scannedQRCodeEditText.setOnTouchListener((vi, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (event.getRawX() >= (scannedQRCodeEditText.getRight() - scannedQRCodeEditText.getCompoundDrawables()[2].getBounds().width())) {
                        Intent intent = new Intent(Engineer.this, QRScanner.class);
                        intent.putExtra("user", receivedUser);
                        qrResultLauncher.launch(intent);
                        return true;
                    }
            }
            return false;
        });

        cancelButton.setOnClickListener(view -> qrDiyalog.dismiss());

        addButton.setOnClickListener(view -> makineEkle(machineTypeSpinner.getSelectedItem().toString(), scannedQRCodeEditText.getText().toString()));

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
                            Toast.makeText(Engineer.this, "QR Code: " + scannedQRCode, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
    );
}