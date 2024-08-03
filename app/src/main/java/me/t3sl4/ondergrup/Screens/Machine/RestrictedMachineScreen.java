package me.t3sl4.ondergrup.Screens.Machine;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Log.Error.ErrorLog;
import me.t3sl4.ondergrup.Screens.Log.Maintenance.MaintenanceLog;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMachineService;
import me.t3sl4.ondergrup.Util.Util;

public class RestrictedMachineScreen extends AppCompatActivity {
    public User receivedUser;
    public Machine selectedMachine;

    private Dialog uyariDiyalog;

    private ImageView backButton;
    private LinearLayout errorLogButton;
    private LinearLayout maintenanceLogButton;
    private LinearLayout changeOwner;

    private ImageView machineImage;
    private TextView ownerName;
    private TextView machineID;
    private TextView cycle;


    private ImageView dilSecimImage;
    private TextView lastUpdate;
    private TextView devirmeYuruyusSecim;
    private TextView calismaSekli;
    private TextView emniyetCercevesi;
    private TextView yavaslamaLimit;
    private TextView altLimit;
    private TextView kapiTablaAcKonum;
    private TextView basincSalteri;
    private TextView kapiSecimleri;
    private TextView kapiAcTipi;
    private TextView kapi1Tip;
    private TextView kapi1AcSure;
    private TextView kapi2Tip;
    private TextView kapi2AcSure;
    private TextView kapitablaTip;
    private TextView kapiTablaAcSure;
    private TextView yukariYavasLimit;
    private TextView devirmeYukariIleriLimit;
    private TextView devirmeAsagiGeriLimit;
    private TextView devirmeSilindirTipi;
    private TextView platformSilindirTipi;
    private TextView yukariValfTmr;
    private TextView asagiValfTmr;
    private TextView devirmeYukariIleriTmr;
    private TextView devirmeAsagiGeriTmr;
    private TextView makineCalismaTmr;
    private TextView buzzer;
    private TextView demoMode;
    private TextView dilSecim;
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
    private TextView lcdBacklightSure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_restricted);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        selectedMachine = intent.getParcelableExtra("machine");

        backButton = findViewById(R.id.backIconImageView);
        errorLogButton = findViewById(R.id.machineErrorLogLayout);
        maintenanceLogButton = findViewById(R.id.machineMaintenanceLogLayout);
        changeOwner = findViewById(R.id.changeMachineOwnerLayout);

        if(Objects.equals(receivedUser.getRole(), "TECHNICIAN")) {
            changeOwner.setVisibility(View.GONE);
        }

        machineImage = findViewById(R.id.makineGorsel);
        ownerName = findViewById(R.id.ownerNameText);
        machineID = findViewById(R.id.machineIDText);
        cycle = findViewById(R.id.machineCycleText);

        dilSecimImage = findViewById(R.id.dilSecimImage);
        devirmeYuruyusSecim = findViewById(R.id.devirmeYuruyusSecim);
        calismaSekli = findViewById(R.id.calismaSekli);
        emniyetCercevesi = findViewById(R.id.emniyetCercevesi);
        yavaslamaLimit = findViewById(R.id.yavaslamaLimit);
        altLimit = findViewById(R.id.altLimit);
        kapiTablaAcKonum = findViewById(R.id.kapiTablaAcKonum);
        basincSalteri = findViewById(R.id.basincSalteri);
        kapiSecimleri = findViewById(R.id.kapiSecimleri);
        kapiAcTipi = findViewById(R.id.kapiAcTipi);
        kapi1Tip = findViewById(R.id.kapi1Tip);
        kapi1AcSure = findViewById(R.id.kapi1AcSure);
        kapi2Tip = findViewById(R.id.kapi2Tip);
        kapi2AcSure = findViewById(R.id.kapi2AcSure);
        kapitablaTip = findViewById(R.id.kapitablaTip);
        kapiTablaAcSure = findViewById(R.id.kapiTablaAcSure);
        yukariYavasLimit = findViewById(R.id.yukariYavasLimit);
        devirmeYukariIleriLimit = findViewById(R.id.devirmeYukariIleriLimit);
        devirmeAsagiGeriLimit = findViewById(R.id.devirmeAsagiGeriLimit);
        devirmeSilindirTipi = findViewById(R.id.devirmeSilindirTipi);
        platformSilindirTipi = findViewById(R.id.platformSilindirTipi);
        yukariValfTmr = findViewById(R.id.yukariValfTmr);
        asagiValfTmr = findViewById(R.id.asagiValfTmr);
        devirmeYukariIleriTmr = findViewById(R.id.devirmeYukariIleriTmr);
        devirmeAsagiGeriTmr = findViewById(R.id.devirmeAsagiGeriTmr);
        makineCalismaTmr = findViewById(R.id.makineCalismaTmr);
        buzzer = findViewById(R.id.buzzer);
        demoMode = findViewById(R.id.demoMode);
        dilSecim = findViewById(R.id.dilSecim);
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
        lcdBacklightSure = findViewById(R.id.lcdBacklightSure);

        initMachineData();

        backButton.setOnClickListener(v -> {
            finish();
        });

        errorLogButton.setOnClickListener(v -> {
            Intent machineErrorIntent = new Intent(RestrictedMachineScreen.this, ErrorLog.class);
            machineErrorIntent.putExtra("currentmachine", selectedMachine.getMachineID());
            startActivity(machineErrorIntent);
        });

        maintenanceLogButton.setOnClickListener(v -> {
            Intent machineMaintenanceLogIntent = new Intent(RestrictedMachineScreen.this, MaintenanceLog.class);
            machineMaintenanceLogIntent.putExtra("user", receivedUser);
            machineMaintenanceLogIntent.putExtra("currentmachine", selectedMachine.getMachineID());
            startActivity(machineMaintenanceLogIntent);
        });

        changeOwner.setOnClickListener(v -> {
            ownerUpdate(selectedMachine.getMachineID());
        });
    }

    private void initMachineData() {
        String machineType = selectedMachine.getMachineType();
        String cycleCount;
        if(selectedMachine.getDemoMode() != null) {
            if(selectedMachine.getDemoMode().equals("1")) {
                cycleCount = selectedMachine.getCalismaSayisiDemo();
            } else {
                cycleCount = selectedMachine.getCalismaSayisi();
            }
        } else {
            cycleCount = "";
        }
        String secilenDil = selectedMachine.getDilSecim();
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

        devirmeYuruyusSecim.setText(selectedMachine.getDevirmeYuruyusSecim());
        calismaSekli.setText(selectedMachine.getCalismaSekli());
        emniyetCercevesi.setText(selectedMachine.getEmniyetCercevesi());
        yavaslamaLimit.setText(selectedMachine.getYavaslamaLimit());
        altLimit.setText(selectedMachine.getAltLimit());
        kapiTablaAcKonum.setText(selectedMachine.getKapiTablaAcKonum());
        basincSalteri.setText(selectedMachine.getBasincSalteri());
        kapiSecimleri.setText(selectedMachine.getKapiSecimleri());
        kapiAcTipi.setText(selectedMachine.getKapiAcTipi());
        kapi1Tip.setText(selectedMachine.getKapi1Tip());
        kapi1AcSure.setText(selectedMachine.getKapi1AcSure());
        kapi2Tip.setText(selectedMachine.getKapi2Tip());
        kapi2AcSure.setText(selectedMachine.getKapi2AcSure());
        kapitablaTip.setText(selectedMachine.getKapitablaTip());
        kapiTablaAcSure.setText(selectedMachine.getKapiTablaAcSure());
        yukariYavasLimit.setText(selectedMachine.getYukariYavasLimit());
        devirmeYukariIleriLimit.setText(selectedMachine.getDevirmeYukariIleriLimit());
        devirmeAsagiGeriLimit.setText(selectedMachine.getDevirmeAsagiGeriLimit());
        devirmeSilindirTipi.setText(selectedMachine.getDevirmeSilindirTipi());
        platformSilindirTipi.setText(selectedMachine.getPlatformSilindirTipi());
        yukariValfTmr.setText(selectedMachine.getYukariValfTmr());
        asagiValfTmr.setText(selectedMachine.getAsagiValfTmr());
        devirmeYukariIleriTmr.setText(selectedMachine.getDevirmeYukariIleriTmr());
        devirmeAsagiGeriTmr.setText(selectedMachine.getDevirmeAsagiGeriTmr());
        makineCalismaTmr.setText(selectedMachine.getMakineCalismaTmr());
        buzzer.setText(selectedMachine.getBuzzer());
        demoMode.setText(selectedMachine.getDemoMode());
        dilSecim.setText(selectedMachine.getDilSecim());
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
        String sure = selectedMachine.getLcdBacklightSure() + " " + this.getResources().getString(R.string.time);
        lcdBacklightSure.setText(sure);

        if(Objects.equals(secilenDil, "0")) {
            selectedLang = ContextCompat.getDrawable(this, R.drawable.ikon_turkish);
        } else if(Objects.equals(secilenDil, "1")) {
            selectedLang = ContextCompat.getDrawable(this, R.drawable.ikon_english);
        }

        dilSecimImage.setImageDrawable(selectedLang);
    }

    private void ownerUpdate(String machineID) {
        Dialog roleDialog = new Dialog(this);
        roleDialog.setContentView(R.layout.activity_popup_owner);
        roleDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        Button ownerButton = roleDialog.findViewById(R.id.ownerButton);
        EditText newOwner = roleDialog.findViewById(R.id.newOwner);

        ownerButton.setOnClickListener(v -> {
            if(newOwner.getText() == null) {
                String errorMessage = this.getResources().getString(R.string.cantbenull);
                Util.showErrorPopup(uyariDiyalog, errorMessage);
                roleDialog.dismiss();
            } else {
                String updateOwnerBody = "{\"machineID\": \"" + machineID + "\", \"userName\": \"" + newOwner.getText().toString() + "\"}";
                sendOwnerRequest(updateOwnerBody);
                ownerName.setText(newOwner.getText().toString());
                roleDialog.dismiss();
            }
        });

        roleDialog.show();
    }

    private void sendOwnerRequest(String updateBody) {
        try {
            JSONObject jsonObject = new JSONObject(updateBody);
            String machineID = jsonObject.getString("machineID");
            String userName = jsonObject.getString("userName");

            OPMachineService.updateOwner(
                    this,
                    machineID,
                    userName,
                    () -> Util.showSuccessPopup(uyariDiyalog, "Makine sahibi başarılı bir şekilde güncellendi."),
                    () -> {
                        Log.d("updateOwner", "Auth Token: " + UserDataService.getAccessToken(this) + " MachineID: " + machineID + " UserName: " + userName);
                        Util.showErrorPopup(uyariDiyalog, "Makine sahibi güncellenemedi. \nLütfen bilgilerinizi kontrol edip tekrar deneyin.");
                    }
            );
        } catch (JSONException e) {
            e.printStackTrace();
            Util.showErrorPopup(uyariDiyalog, "Güncelleme verisi işlenirken bir hata oluştu.");
        }
    }
}