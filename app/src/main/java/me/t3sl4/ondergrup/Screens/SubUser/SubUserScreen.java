package me.t3sl4.ondergrup.Screens.SubUser;

import android.annotation.SuppressLint;
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
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.Screens.Support.SupportScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private Dialog uyariDiyalog;
    private Dialog qrDiyalog;

    private Button subUserListeleButton;
    private Button subUsterEkleButton;

    private ConstraintLayout homeButton;
    private ConstraintLayout profileButton;
    private ConstraintLayout machineButton;
    private ConstraintLayout settingsButton;
    private FloatingActionButton qrButton;

    public static String scannedQRCode;
    public static EditText scannedQRCodeEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser);

        uyariDiyalog = new Dialog(this);
        qrDiyalog = new Dialog(this);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        subUserListeleButton = findViewById(R.id.showButton);
        subUsterEkleButton = findViewById(R.id.addSubUserButton);

        homeButton = findViewById(R.id.mainConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        machineButton = findViewById(R.id.machineConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        qrButton = findViewById(R.id.qrConstraint);

        subUserListeleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserListScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        subUsterEkleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserAddScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(SubUserScreen.this, DashboardUserScreen.class);
            homeIntent.putExtra("user", receivedUser);
            startActivity(homeIntent);
            finish();
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(SubUserScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        });

        machineButton.setOnClickListener(v -> {
            //TODO
            //Makinelerim ekranı
        });

        settingsButton.setOnClickListener(v -> {
            Intent editProfileIntent = new Intent(SubUserScreen.this, EditProfileScreen.class);
            editProfileIntent.putExtra("user", receivedUser);
            startActivity(editProfileIntent);
        });

        qrButton.setOnClickListener(v -> {
            if(receivedUser.getRole().equals("NORMAL")) {
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
                                Intent qrIntent = new Intent(SubUserScreen.this, QRScanner.class);
                                qrIntent.putExtra("fromScreen", "SubUser");
                                startActivity(qrIntent);
                                return true;
                            }
                    }
                    return false;
                });

                cancelButton.setOnClickListener(view -> qrDiyalog.dismiss());

                addButton.setOnClickListener(view -> makineEkle(machineTypeSpinner.getSelectedItem().toString(), scannedQRCode));

                String[] machineTypes = getResources().getStringArray(R.array.machineType);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, machineTypes);
                machineTypeSpinner.setAdapter(adapter);

                qrDiyalog.show();
                qrDiyalog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                qrDiyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                qrDiyalog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                qrDiyalog.getWindow().setGravity(Gravity.BOTTOM);
            } else {
                util.showErrorPopup(uyariDiyalog, "Sadece NORMAL kullanıcılar doğrudan makine ekleyebilir.");
            }
        });
    }

    public void makineEkle(String machineType, String machineID) {
        String reqURL = util.BASE_URL + util.addMachineURL;

        String userName = receivedUser.getUserName();
        String companyName = receivedUser.getCompanyName();
        String jsonAddMachineBody = "{\"Username\": \"" + userName + "\", \"CompanyName\": \"" + companyName + "\", \"MachineID\": \"" + machineID + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(reqURL, jsonAddMachineBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                qrDiyalog.dismiss();
                util.showSuccessPopup(uyariDiyalog, "Makine başarılı bir şekilde eklendi.");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı adı veya şifreniz hatalı. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
            }
        });
    }
}
