package me.t3sl4.ondergrup.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Documents.DocumentsScreen;
import me.t3sl4.ondergrup.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.Screens.MainActivity;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.Screens.Support.SupportScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class DashboardUserScreen extends AppCompatActivity {
    private static final String TARGET_WIFI_SSID = "OnderGrup_IoT";
    public Util util;

    private TextView isimSoyisim;
    private ImageView profilePhotoView;

    private ConstraintLayout profileButton;
    private LinearLayout destekButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout belgelerButton;
    private ConstraintLayout subUserButton;
    private ConstraintLayout machineManageButton;
    private ConstraintLayout myMachineButton;
    private FloatingActionButton qrButton;

    public User receivedUser;

    private Dialog uyariDiyalog;
    private Dialog qrDiyalog;

    public static String scannedQRCode;
    public static EditText scannedQRCodeEditText;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);
        qrDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        isimSoyisim = findViewById(R.id.textView4);
        profilePhotoView = findViewById(R.id.imageView4);

        profileButton = findViewById(R.id.profileConstraint);
        destekButton = findViewById(R.id.destekButton);
        settingsButton = findViewById(R.id.settingsConstraint);
        belgelerButton = findViewById(R.id.belgelerConstraint);
        subUserButton = findViewById(R.id.subUserConstraint);
        machineManageButton = findViewById(R.id.machineManageConstraint);
        myMachineButton = findViewById(R.id.myMachine);
        qrButton = findViewById(R.id.qrConstraint);

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(DashboardUserScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
            finish();
        });

        destekButton.setOnClickListener(v -> {
            Intent destekIntent = new Intent(DashboardUserScreen.this, SupportScreen.class);
            destekIntent.putExtra("user", util.user);
            startActivity(destekIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(DashboardUserScreen.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });

        belgelerButton.setOnClickListener(v -> {
            Intent belgelerIntent = new Intent(DashboardUserScreen.this, DocumentsScreen.class);
            startActivity(belgelerIntent);
        });

        subUserButton.setOnClickListener(v -> {
            if(receivedUser.getOwnerName() != null) {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcıları yalnızca yöneticiniz görüntüleyebilir.");
            } else {
                Intent settingsIntent = new Intent(DashboardUserScreen.this, SubUserScreen.class);
                settingsIntent.putExtra("user", receivedUser);
                startActivity(settingsIntent);
            }
        });

        machineManageButton.setOnClickListener(v -> {
           Intent manageMachineIntent = new Intent(DashboardUserScreen.this, MachineListScreen.class);
           manageMachineIntent.putExtra("user", receivedUser);
           startActivity(manageMachineIntent);
        });

        myMachineButton.setOnClickListener(v -> {
            Intent manageMachineIntent = new Intent(DashboardUserScreen.this, MachineListScreen.class);
            manageMachineIntent.putExtra("user", receivedUser);
            startActivity(manageMachineIntent);
        });

        qrButton.setOnClickListener(v -> {
            if(receivedUser.getRole().equals("NORMAL")) {
                qrDiyalog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                qrDiyalog.setContentView(R.layout.activity_machine_add);

                ImageView cancelButton = qrDiyalog.findViewById(R.id.cancelButton);
                ImageView wifiButton = qrDiyalog.findViewById(R.id.wifiButton);
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
                                Intent qrIntent = new Intent(DashboardUserScreen.this, QRScanner.class);
                                qrIntent.putExtra("fromScreen", "Support");
                                startActivity(qrIntent);
                                return true;
                            }
                    }
                    return false;
                });

                cancelButton.setOnClickListener(view -> qrDiyalog.dismiss());

                wifiButton.setOnClickListener(view -> {
                    if (!isConnectedToTargetWifi()) {
                        openWifiSettings();

                        new Thread(() -> {
                            while (!isConnectedToTargetWifi()) {
                                try {
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            runOnUiThread(() -> {
                                wifiButton.setImageDrawable(getResources().getDrawable(R.drawable.wifi_green));
                            });
                        }).start();
                    } else {
                        wifiButton.setImageDrawable(getResources().getDrawable(R.drawable.wifi_green));
                    }
                });


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

        setUserInfo();
    }

    public void setUserInfo() {
        isimSoyisim.setText(receivedUser.getNameSurname());
        String imageUrl = util.BASE_URL + util.getPhotoURLPrefix + receivedUser.getUserName() + ".jpg";

        Glide.with(this)
                .load(imageUrl).override(100,100)
                .into(profilePhotoView);

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

    private boolean isConnectedToTargetWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                if (wifiManager != null) {
                    String ssid = wifiManager.getConnectionInfo().getSSID().replace("\"", ""); // Remove quotes from SSID
                    return ssid.equals(TARGET_WIFI_SSID);
                }
            }
        }
        return false;
    }

    private void openWifiSettings() {
        Intent intent = new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK);
        startActivity(intent);
        finish();
    }
}