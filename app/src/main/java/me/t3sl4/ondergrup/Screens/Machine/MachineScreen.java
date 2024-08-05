package me.t3sl4.ondergrup.Screens.Machine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import java.util.Objects;

import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Log.Error.ErrorLog;
import me.t3sl4.ondergrup.Screens.Log.Maintenance.MaintenanceLog;

public class MachineScreen extends AppCompatActivity {
    public User receivedUser;
    public Machine selectedMachine;

    private Dialog uyariDiyalog;

    private ImageView backButton;
    private LinearLayout errorLogButton;
    private LinearLayout maintenanceLogButton;

    private ImageView machineImage;
    private TextView ownerName;
    private TextView machineID;
    private TextView cycle;


    private ImageView dilSecimImage;
    private TextView lastUpdate;
    private TextView eepromData38;
    private TextView eepromData39;
    private TextView eepromData40;
    private TextView eepromData41;
    private TextView eepromData42;
    private TextView eepromData43;
    private TextView eepromData44;
    private TextView eepromData45;
    private TextView eepromData46;
    private TextView eepromData47;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        selectedMachine = intent.getParcelableExtra("machine");

        initializeComponents();

        initMachineData();

        buttonClickListeners();
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.backIconImageView);
        errorLogButton = findViewById(R.id.machineErrorLogLayout);
        maintenanceLogButton = findViewById(R.id.machineMaintenanceLogLayout);

        machineImage = findViewById(R.id.makineGorsel);
        ownerName = findViewById(R.id.ownerNameText);
        machineID = findViewById(R.id.machineIDText);
        cycle = findViewById(R.id.machineCycleText);

        dilSecimImage = findViewById(R.id.dilSecimImage);
        eepromData38 = findViewById(R.id.eepromData38);
        eepromData39 = findViewById(R.id.eepromData39);
        eepromData40 = findViewById(R.id.eepromData40);
        eepromData41 = findViewById(R.id.eepromData41);
        eepromData42 = findViewById(R.id.eepromData42);
        eepromData43 = findViewById(R.id.eepromData43);
        eepromData44 = findViewById(R.id.eepromData44);
        eepromData45 = findViewById(R.id.eepromData45);
        eepromData46 = findViewById(R.id.eepromData46);
        eepromData47 = findViewById(R.id.eepromData47);
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> {
            finish();
        });

        errorLogButton.setOnClickListener(v -> {
            Intent machineErrorIntent = new Intent(MachineScreen.this, ErrorLog.class);
            machineErrorIntent.putExtra("currentmachine", selectedMachine.getMachineID());
            startActivity(machineErrorIntent);
        });

        maintenanceLogButton.setOnClickListener(v -> {
            Intent machineMaintenanceLogIntent = new Intent(MachineScreen.this, MaintenanceLog.class);
            machineMaintenanceLogIntent.putExtra("user", receivedUser);
            machineMaintenanceLogIntent.putExtra("currentmachine", selectedMachine.getMachineID());
            startActivity(machineMaintenanceLogIntent);
        });
    }

    private void initMachineData() {
        String machineType = selectedMachine.getMachineType();
        String cycleCount = this.getResources().getString(R.string.machine_data_null);
        if(selectedMachine.getDemoMode() != null) {
            if(selectedMachine.getDemoMode().equals("1")) {
                cycleCount = selectedMachine.getCalismaSayisiDemo();
            } else if(selectedMachine.getDemoMode().equals("0")) {
                cycleCount = selectedMachine.getCalismaSayisi();
            }
        }

        String secilenDil = this.getResources().getString(R.string.machine_data_null);
        if(selectedMachine.getDilSecim() != null) {
            if(selectedMachine.getDilSecim().equals("1") || selectedMachine.getDilSecim().equals("0")) {
                secilenDil = selectedMachine.getDilSecim();
            }
        }

        Drawable drawable = null;
        Drawable selectedLang = null;
        if(Objects.equals(machineType, "ESP")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.tanitimekrani_esp1, null);
        } else if(Objects.equals(machineType, "CSP-D")) {
            drawable = ResourcesCompat.getDrawable(getResources(), R.drawable.tanitimekrani_csp1, null);
        }

        machineImage.setImageDrawable(drawable);
        ownerName.setText(selectedMachine.getOwnerUserName());
        machineID.setText(selectedMachine.getMachineID());
        cycle.setText(cycleCount);

        eepromData38.setText(selectedMachine.getEepromData38());
        eepromData39.setText(selectedMachine.getEepromData39());
        eepromData40.setText(selectedMachine.getEepromData40());
        eepromData41.setText(selectedMachine.getEepromData41());
        eepromData42.setText(selectedMachine.getEepromData42());
        eepromData43.setText(selectedMachine.getEepromData43());
        eepromData44.setText(selectedMachine.getEepromData44());
        eepromData45.setText(selectedMachine.getEepromData45());
        eepromData46.setText(selectedMachine.getEepromData46());
        eepromData47.setText(selectedMachine.getEepromData47());

        if(Objects.equals(secilenDil, "0")) {
            selectedLang = ContextCompat.getDrawable(this, R.drawable.ikon_turkish);
        } else if(Objects.equals(secilenDil, "1")) {
            selectedLang = ContextCompat.getDrawable(this, R.drawable.ikon_english);
        }

        dilSecimImage.setImageDrawable(selectedLang);
    }
}
