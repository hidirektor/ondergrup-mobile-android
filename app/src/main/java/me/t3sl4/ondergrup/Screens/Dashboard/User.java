package me.t3sl4.ondergrup.Screens.Dashboard;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

import me.t3sl4.ondergrup.Model.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.Documents.DocumentsScreen;
import me.t3sl4.ondergrup.Screens.Log.Error.ErrorLogAll;
import me.t3sl4.ondergrup.Screens.Log.Maintenance.MaintenanceLogAll;
import me.t3sl4.ondergrup.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.Screens.Machine.MachineScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.QR.QRScanner;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.Util.Component.Navigation.NavigationManager;
import me.t3sl4.ondergrup.Util.Component.SharedPreferencesManager;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.Util;

public class User extends AppCompatActivity {
    private static final String TARGET_WIFI_SSID = "OnderGrup_IoT";
    public Util util;

    private TextView isimSoyisim;

    private ImageView hamburgerButton;
    private NavigationView hamburgerMenu;
    private ConstraintLayout subLanguage;
    private ConstraintLayout profileButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout belgelerButton;
    private ConstraintLayout subUserButton;
    private ConstraintLayout allErrorsButton;
    private ConstraintLayout allMaintenancesButton;
    private ConstraintLayout myMachineButton;
    private FloatingActionButton qrButton;

    //hamburgerButtons
    private Button navAddMachineButton;
    private LinearLayout navProfileButton;
    private LinearLayout navDocsButton;
    private LinearLayout navSettingsButton;
    private LinearLayout navLanguageButton;
    private TextView navCurrentLang;
    private LinearLayout feedbackButton;
    private LinearLayout logoutButton;

    //Hamburger Restriction
    private ConstraintLayout headerConstraint;
    private LinearLayout headerLayout;
    private LinearLayout machineLayout;
    private LinearLayout machineInnerLayout;
    private CoordinatorLayout subLayout;


    //Machine List View Section:
    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    public me.t3sl4.ondergrup.Model.User.User receivedUser;

    private Dialog uyariDiyalog;
    private Dialog qrDiyalog;

    public static String scannedQRCode;
    public static EditText scannedQRCodeEditText;

    private String currentLang;

    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("MainActivity", "Tarama iptal edildi");
                        Toast.makeText(User.this, "Tarama kapatıldı", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Kamera yetkisi eksik");
                        String cameraPerm = getResources().getString(R.string.camera_permission_error);
                        util.showErrorPopup(uyariDiyalog, cameraPerm);
                    }
                } else {
                    Log.d("MainActivity", "Taramam tamamlandı");
                    scannedQRCode = result.getContents();
                    scannedQRCodeEditText.setText(scannedQRCode);
                    Toast.makeText(User.this, "ID: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            });

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        util = new Util(this);
        uyariDiyalog = new Dialog(this);
        qrDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        currentLang = SharedPreferencesManager.getSharedPref("language", User.this, "en");

        isimSoyisim = findViewById(R.id.loggedUserName);

        hamburgerButton = findViewById(R.id.hamburgerMenuBttn);
        hamburgerMenu = findViewById(R.id.hamburgerMenu);
        subLanguage = findViewById(R.id.languageConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);
        belgelerButton = findViewById(R.id.belgelerConstraint);
        subUserButton = findViewById(R.id.subUserConstraint);
        allErrorsButton = findViewById(R.id.allErrorsConstraint);
        allMaintenancesButton = findViewById(R.id.allMaintenancesConstraint);
        myMachineButton = findViewById(R.id.myMachine);
        qrButton = findViewById(R.id.qrConstraint);

        //restriction
        headerConstraint = findViewById(R.id.headerConstraint);
        headerLayout = findViewById(R.id.headerLayout);
        machineLayout = findViewById(R.id.machineLayout);
        machineInnerLayout = findViewById(R.id.machineInnerLayout);
        subLayout = findViewById(R.id.subLayout);

        hamburgerButton.setOnClickListener(v -> {
            NavigationManager.showNavigationViewWithAnimation(hamburgerMenu, this);
            //minimizeMainLayout();
        });

        //hamburgerButtons
        View hamburgerView = hamburgerMenu.getHeaderView(0);
        navAddMachineButton = hamburgerView.findViewById(R.id.navAddMachineButton);
        navProfileButton = hamburgerView.findViewById(R.id.navProfileButton);
        navDocsButton = hamburgerView.findViewById(R.id.navDocsButton);
        navSettingsButton = hamburgerView.findViewById(R.id.navSettingsButton);
        navLanguageButton = hamburgerView.findViewById(R.id.navLanguageButton);
        navCurrentLang = hamburgerView.findViewById(R.id.current_lang);
        feedbackButton = hamburgerView.findViewById(R.id.feedbackButton);
        logoutButton = hamburgerView.findViewById(R.id.logoutButton);

        String activeText = "";
        if(Objects.equals(currentLang, "tr")) {
            activeText = this.getResources().getString(R.string.active_language) + " " + this.getResources().getString(R.string.lang_turkish);
        } else {
            activeText = this.getResources().getString(R.string.active_language) + " " + this.getResources().getString(R.string.lang_english);
        }

        navCurrentLang.setText(activeText);

        navProfileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(User.this, ProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
        });
        subLanguage.setOnClickListener(v -> {
            switchLanguage();
        });
        navSettingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(User.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });
        navDocsButton.setOnClickListener(v -> {
            Intent belgelerIntent = new Intent(User.this, DocumentsScreen.class);
            startActivity(belgelerIntent);
        });
        navAddMachineButton.setOnClickListener(v -> {
            NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
            //expandMainLayout();
            addMachine();
        });

        navLanguageButton.setOnClickListener(v -> {
            switchLanguage();
        });

        logoutButton.setOnClickListener(v -> logoutProcess());

        feedbackButton.setOnClickListener(v -> {
            String url = "https://play.google.com/store/apps/details?id=me.t3sl4.ondergrup&hl=tr&gl=US";
            Intent playStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(playStoreIntent);
        });

        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();
        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(User.this, MachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(User.this, ProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(User.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", util.user);
            startActivity(settingsIntent);
        });

        belgelerButton.setOnClickListener(v -> {
            Intent belgelerIntent = new Intent(User.this, DocumentsScreen.class);
            startActivity(belgelerIntent);
        });
 
        subUserButton.setOnClickListener(v -> {
            if(receivedUser.getOwnerName() != null) {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcıları yalnızca yöneticiniz görüntüleyebilir.");
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

        setUserInfo();

        hamburgerEffect();
    }

    public void setUserInfo() {
        isimSoyisim.setText(this.getResources().getString(R.string.hello_prefix) + receivedUser.getNameSurname());
    }

    public void makineEkle(String machineType, String machineID) {
        String reqURL = util.BASE_URL + util.addMachineURL;

        String userName = receivedUser.getUserName();
        String companyName = receivedUser.getCompanyName();
        String jsonAddMachineBody = "{\"Username\": \"" + userName + "\", \"CompanyName\": \"" + companyName + "\", \"MachineID\": \"" + machineID + "\", \"MachineType\": \"" + machineType + "\"}";

        HTTP.sendRequest(reqURL, jsonAddMachineBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                qrDiyalog.dismiss();
                util.showSuccessPopup(uyariDiyalog, "Makine başarılı bir şekilde eklendi.");
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kullanıcı adı veya şifreniz hatalı. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
            }
        }, Volley.newRequestQueue(this));
    }

    private boolean isConnectedToTargetWifi() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
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
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();
        String reqURL = util.BASE_URL + util.getMachineURL;
        String jsonSubUserBody = "{\"username\": \"" + receivedUser.getUserName() + "\"}";

        HTTP.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray machineArray = response.getJSONArray("machines");
                    for (int i = 0; i < machineArray.length(); i++) {
                        JSONObject machineInfoObj = machineArray.getJSONObject(i).getJSONObject("MachineInfo");
                        JSONObject machineDataObj = machineArray.getJSONObject(i).getJSONArray("MachineData").getJSONObject(0);

                        String ownerUser = machineInfoObj.getString("Owner_UserName");
                        String lastUpdate = machineInfoObj.getString("LastUpdate");
                        String machineType = machineInfoObj.getString("MachineType");
                        String machineID = machineInfoObj.getString("MachineID");
                        String devirmeYuruyusSecim = machineDataObj.getString("devirmeYuruyusSecim");
                        String calismaSekli = machineDataObj.getString("calismaSekli");
                        String emniyetCercevesi = machineDataObj.getString("emniyetCercevesi");
                        String yavaslamaLimit = machineDataObj.getString("yavaslamaLimit");
                        String altLimit = machineDataObj.getString("altLimit");
                        String kapiTablaAcKonum = machineDataObj.getString("kapiTablaAcKonum");
                        String basincSalteri = machineDataObj.getString("basincSalteri");
                        String kapiSecimleri = machineDataObj.getString("kapiSecimleri");
                        String kapiAcTipi = machineDataObj.getString("kapiAcTipi");
                        String kapi1Tip = machineDataObj.getString("kapi1Tip");
                        String kapi1AcSure = machineDataObj.getString("kapi1AcSure");
                        String kapi2Tip = machineDataObj.getString("kapi2Tip");
                        String kapi2AcSure = machineDataObj.getString("kapi2AcSure");
                        String kapitablaTip = machineDataObj.getString("kapitablaTip");
                        String kapiTablaAcSure = machineDataObj.getString("kapiTablaAcSure");
                        String yukariYavasLimit = machineDataObj.getString("yukariYavasLimit");
                        String devirmeYukariIleriLimit = machineDataObj.getString("devirmeYukariIleriLimit");
                        String devirmeAsagiGeriLimit = machineDataObj.getString("devirmeAsagiGeriLimit");
                        String devirmeSilindirTipi = machineDataObj.getString("devirmeSilindirTipi");
                        String platformSilindirTipi = machineDataObj.getString("platformSilindirTipi");
                        String yukariValfTmr = machineDataObj.getString("yukariValfTmr");
                        String asagiValfTmr = machineDataObj.getString("asagiValfTmr");
                        String devirmeYukariIleriTmr = machineDataObj.getString("devirmeYukariIleriTmr");
                        String devirmeAsagiGeriTmr = machineDataObj.getString("devirmeAsagiGeriTmr");
                        String makineCalismaTmr = machineDataObj.getString("makineCalismaTmr");
                        String buzzer = machineDataObj.getString("buzzer");
                        String demoMode = machineDataObj.getString("demoMode");
                        String calismaSayisi1 = machineDataObj.getString("calismaSayisi1");
                        String calismaSayisi10 = machineDataObj.getString("calismaSayisi10");
                        String calismaSayisi100 = machineDataObj.getString("calismaSayisi100");
                        String calismaSayisi1000 = machineDataObj.getString("calismaSayisi1000");
                        String calismaSayisi10000 = machineDataObj.getString("calismaSayisi10000");
                        String dilSecim = machineDataObj.getString("dilSecim");
                        String eepromData38 = machineDataObj.getString("eepromData38");
                        String eepromData39 = machineDataObj.getString("eepromData39");
                        String eepromData40 = machineDataObj.getString("eepromData40");
                        String eepromData41 = machineDataObj.getString("eepromData41");
                        String eepromData42 = machineDataObj.getString("eepromData42");
                        String eepromData43 = machineDataObj.getString("eepromData43");
                        String eepromData44 = machineDataObj.getString("eepromData44");
                        String eepromData45 = machineDataObj.getString("eepromData45");
                        String eepromData46 = machineDataObj.getString("eepromData46");
                        String eepromData47 = machineDataObj.getString("eepromData47");
                        String lcdBacklightSure = machineDataObj.getString("lcdBacklightSure");

                        Machine selectedMachine = new Machine(ownerUser, lastUpdate, machineType, machineID, devirmeYuruyusSecim, calismaSekli, emniyetCercevesi,
                                yavaslamaLimit, altLimit, kapiTablaAcKonum, basincSalteri, kapiSecimleri,
                                kapiAcTipi, kapi1Tip, kapi1AcSure, kapi2Tip, kapi2AcSure, kapitablaTip,
                                kapiTablaAcSure, yukariYavasLimit, devirmeYukariIleriLimit, devirmeAsagiGeriLimit,
                                devirmeSilindirTipi, platformSilindirTipi, yukariValfTmr, asagiValfTmr,
                                devirmeYukariIleriTmr, devirmeAsagiGeriTmr, makineCalismaTmr, buzzer, demoMode,
                                calismaSayisi1, calismaSayisi10, calismaSayisi100, calismaSayisi1000, calismaSayisi10000,
                                dilSecim, eepromData38, eepromData39, eepromData40, eepromData41,
                                eepromData42, eepromData43, eepromData44, eepromData45, eepromData46, eepromData47,
                                lcdBacklightSure);
                        machines.add(selectedMachine);
                    }

                    updateListView(machines);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Herhangi bir alt kullanıcı bulunamadı.");
            }
        }, Volley.newRequestQueue(this));
        return machines;
    }

    private void updateListView(ArrayList<Machine> machines) {
        machineList = machines;
        machineListAdapter = new MachineAdapter(this, machineList);
        machineListView.setAdapter(machineListAdapter);
    }

    public void scanBarcodeCustomLayout() {
        ScanOptions options = new ScanOptions().setOrientationLocked(false).setCaptureActivity(QRScanner.class);
        barcodeLauncher.launch(options);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void hamburgerEffect() {
        headerConstraint.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });

        headerLayout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });

        machineLayout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });

        machineInnerLayout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });

        machineListView.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });

        subLayout.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN && hamburgerMenu.getVisibility() == View.VISIBLE) {
                NavigationManager.hideNavigationViewWithAnimation(hamburgerMenu, this);
                //expandMainLayout();
                return true;
            }
            return false;
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void addMachine() {
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
                            scanBarcodeCustomLayout();
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
                            wifiButton.setImageDrawable(getResources().getDrawable(R.drawable.ikon_wifi_green));
                        });
                    }).start();
                } else {
                    wifiButton.setImageDrawable(getResources().getDrawable(R.drawable.ikon_wifi_green));
                }
            });


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
        } else {
            util.showErrorPopup(uyariDiyalog, "Sadece NORMAL kullanıcılar doğrudan makine ekleyebilir.");
        }
    }

    private void logoutProcess() {
        String username = SharedPreferencesManager.getSharedPref("username", this, "");

        if(!username.isEmpty()) {
            SharedPreferencesManager.writeSharedPref("username", "", this);
            SharedPreferencesManager.writeSharedPref("password", "", this);
            SharedPreferencesManager.writeSharedPref("role", "", this);
        }

        Intent loginIntent = new Intent(User.this, LoginScreen.class);
        startActivity(loginIntent);
        finish();
    }

    private void minimizeMainLayout() {
        subLayout.setPadding(200, 200, 200, 200);
        machineLayout.setPadding(200, 200, 200, 200);
        headerLayout.setPadding(200, 200, 200, 200);
    }

    private void expandMainLayout() {
        subLayout.setPadding(0, 0, 0, 0);
        machineLayout.setPadding(0, 0, 0, 0);
        headerLayout.setPadding(0, 0, 0, 0);
    }

    private void switchLanguage() {
        String currentLanguage = SharedPreferencesManager.getSharedPref("language", User.this, "en");
        String nextLang = "";

        if (currentLanguage.equals("tr")) {
            SharedPreferencesManager.writeSharedPref("language", "en", User.this);
            nextLang = "en";
        } else {
            SharedPreferencesManager.writeSharedPref("language", "tr", User.this);
            String trText = this.getResources().getString(R.string.active_language) + " " + this.getResources().getString(R.string.lang_turkish);
            navCurrentLang.setText(trText);
            nextLang = "tr";
        }

        Util.setLocale(User.this, nextLang);
        recreate();
    }
}