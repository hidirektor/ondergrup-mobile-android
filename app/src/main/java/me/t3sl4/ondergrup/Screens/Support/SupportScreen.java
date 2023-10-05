package me.t3sl4.ondergrup.Screens.Support;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.params.ColorSpaceTransform;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardEngineerScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardSysOpScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardTechnicianScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SupportScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ConstraintLayout homeButton;
    private ConstraintLayout profileButton;
    private ConstraintLayout machineButton;
    private ConstraintLayout settingsButton;
    private FloatingActionButton qrButton;

    private Button mapButton;
    private Button mailButton;
    private Button callButton;

    public static String scannedQRCode;
    public static EditText scannedQRCodeEditText;

    private Dialog uyariDiyalog;
    private Dialog qrDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_support_selection);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        uyariDiyalog = new Dialog(this);
        qrDiyalog = new Dialog(this);

        homeButton = findViewById(R.id.mainConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        machineButton = findViewById(R.id.machineConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        mapButton = findViewById(R.id.mapButton);
        mailButton = findViewById(R.id.mailButton);
        callButton = findViewById(R.id.callButton);
        qrButton = findViewById(R.id.qrConstraint);

        homeButton.setOnClickListener(v -> {
            geriDon();
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(SupportScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
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
                                Intent qrIntent = new Intent(SupportScreen.this, QRScanner.class);
                                qrIntent.putExtra("fromScreen", "Support");
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

        machineButton.setOnClickListener(v -> {
            //TODO
            //Makinelerim ekranı:
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(SupportScreen.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });

        mapButton.setOnClickListener(v -> {
            showMap();
        });

        mailButton.setOnClickListener(v -> {
            mailUs();
        });

        callButton.setOnClickListener(v -> {
            callUs();
        });
    }

    public void addMachine() {
        //TODO
        // Makine ekleme ekranı kodları
    }

    public void callUs() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:+902363325050"));

            startActivity(callIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
        }
    }

    public void mailUs() {
        String url = "https://www.ondergrup.com/iletisim/";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void showMap() {
        String url = "https://www.google.com/maps/place/%C3%96nder+Lift+%C3%87elik+Mak.+San.+Tic.+Ltd.+%C5%9Eti./@38.4851028,27.6519011,15z/data=!4m2!3m1!1s0x0:0x8f19e57eecad8dea?sa=X&ved=2ahUKEwiKkv2josuBAxWyS_EDHRHhD1gQ_BJ6BAhMEAA&ved=2ahUKEwiKkv2josuBAxWyS_EDHRHhD1gQ_BJ6BAhdEAg";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    public void geriDon() {
        String role = receivedUser.getRole();
        if(role == "NORMAL") {
            Intent mainIntent = new Intent(SupportScreen.this, DashboardUserScreen.class);
            startActivity(mainIntent);
            finish();
        } else if(role == "TECHNICIAN") {
            Intent mainIntent = new Intent(SupportScreen.this, DashboardTechnicianScreen.class);
            startActivity(mainIntent);
            finish();
        } else if(role == "ENGINEER") {
            Intent mainIntent = new Intent(SupportScreen.this, DashboardEngineerScreen.class);
            startActivity(mainIntent);
            finish();
        } else {
            Intent mainIntent = new Intent(SupportScreen.this, DashboardSysOpScreen.class);
            startActivity(mainIntent);
            finish();
        }
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
