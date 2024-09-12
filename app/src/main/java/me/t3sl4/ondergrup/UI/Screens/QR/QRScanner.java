package me.t3sl4.ondergrup.UI.Screens.QR;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;

import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.Settings.SettingsDashboard;
import me.t3sl4.ondergrup.UI.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.UI.Screens.SupportTicket.SupportTickets;
import me.t3sl4.ondergrup.Util.Util;

public class QRScanner extends FragmentActivity {

    /*
    User Section
     */
    private LinearLayout userLinearLayout;
    private ConstraintLayout myMachine;
    private ConstraintLayout supportConstraint;
    private ConstraintLayout subUserConstraint;
    private ConstraintLayout settingsConstraint;

    /*
    Engineer Section
     */
    private LinearLayout engineerLinearLayout;
    private ConstraintLayout allMachines;
    private ConstraintLayout profileConstraint;
    private ConstraintLayout languageConstraint;
    private ConstraintLayout engineerSettingsConstraint;

    public User receivedUser;
    private Dialog uyariDiyalog;
    String currentUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_barcode_scanner);

        ScanFragment scanFragment = new ScanFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frag_container, scanFragment)
                .commit();

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        uyariDiyalog = new Dialog(this);
        currentUserRole = UserDataService.getUserRole(this);

        initializeComponents();

        buttonClickHandlers();
    }

    private void initializeComponents() {
        userLinearLayout = findViewById(R.id.userLinearLayout);
        myMachine = findViewById(R.id.myMachine);
        supportConstraint = findViewById(R.id.supportConstraint);
        subUserConstraint = findViewById(R.id.subUserConstraint);
        settingsConstraint = findViewById(R.id.settingsConstraint);

        engineerLinearLayout = findViewById(R.id.engineerLinearLayout);
        allMachines = findViewById(R.id.allMachines);
        profileConstraint = findViewById(R.id.profileConstraint);
        languageConstraint = findViewById(R.id.languageConstraint);
        engineerSettingsConstraint = findViewById(R.id.engineerSettingsConstraint);

        if(currentUserRole.equals("NORMAL") || currentUserRole.equals("SUBUSER")) {
            userLinearLayout.setVisibility(View.VISIBLE);
            engineerLinearLayout.setVisibility(View.GONE);
        } else if(currentUserRole.equals("ENGINEER")) {
            userLinearLayout.setVisibility(View.GONE);
            engineerLinearLayout.setVisibility(View.VISIBLE);
        }
    }

    private void buttonClickHandlers() {
        /*
        User Section
         */
        myMachine.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(QRScanner.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
            finish();
        });

        supportConstraint.setOnClickListener(v -> {
            Intent profileIntent = new Intent(QRScanner.this, SupportTickets.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        });

        subUserConstraint.setOnClickListener(v -> {
            if(receivedUser.getOwnerName() != null) {
                Util.showErrorPopup(uyariDiyalog, this.getResources().getString(R.string.subuser_error));
            } else {
                Intent settingsIntent = new Intent(QRScanner.this, SubUserScreen.class);
                settingsIntent.putExtra("user", receivedUser);
                startActivity(settingsIntent);
                finish();
            }
        });

        settingsConstraint.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(QRScanner.this, SettingsDashboard.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });

        /*
        Engineer Section
         */
        allMachines.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(QRScanner.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
            finish();
        });

        profileConstraint.setOnClickListener(v -> {
            Intent profileIntent = new Intent(QRScanner.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        });

        languageConstraint.setOnClickListener(v -> {
            Util.changeSystemLanguage(this);
            recreate();
        });

        engineerSettingsConstraint.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(QRScanner.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });
    }
}