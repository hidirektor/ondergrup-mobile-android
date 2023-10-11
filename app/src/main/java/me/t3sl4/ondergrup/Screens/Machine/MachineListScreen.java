package me.t3sl4.ondergrup.Screens.Machine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Machine.Adapter.Machine;
import me.t3sl4.ondergrup.Screens.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUser;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUserAdapter;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class MachineListScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_list);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();
    }

    private ArrayList<Machine> getMachineList() {
        ArrayList<Machine> machines = new ArrayList<>();
        String reqURL = util.BASE_URL + util.getSubUsersPrefix;
        String jsonSubUserBody = "{\"username\": \"" + receivedUser.getUserName() + "\"}";

        HTTP http = new HTTP(this);
        http.sendRequest(reqURL, jsonSubUserBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray machineArray = response.getJSONArray("machines");
                    for (int i = 0; i < machineArray.length(); i++) {
                        JSONObject machineObj = machineArray.getJSONObject(i);
                        String ownerUser = machineObj.getString("ownerUser");
                        String lastUpdate = machineObj.getString("lastUpdate");
                        String machineType = machineObj.getString("machineType");
                        String machineID = machineObj.getString("machineID");
                        String devirmeYuruyusSecim = machineObj.getString("devirmeYuruyusSecim");
                        String calismaSekli = machineObj.getString("calismaSekli");
                        String emniyetCercevesi = machineObj.getString("emniyetCercevesi");
                        String yavaslamaLimit = machineObj.getString("yavaslamaLimit");
                        String altLimit = machineObj.getString("altLimit");
                        String kapiTablaAcKonum = machineObj.getString("kapiTablaAcKonum");
                        String basincSalteri = machineObj.getString("basincSalteri");
                        String kapiSecimleri = machineObj.getString("kapiSecimleri");
                        String kapiAcTipi = machineObj.getString("kapiAcTipi");
                        String kapi1Tip = machineObj.getString("kapi1Tip");
                        String kapi1AcSure = machineObj.getString("kapi1AcSure");
                        String kapi2Tip = machineObj.getString("kapi2Tip");
                        String kapi2AcSure = machineObj.getString("kapi2AcSure");
                        String kapitablaTip = machineObj.getString("kapitablaTip");
                        String kapiTablaAcSure = machineObj.getString("kapiTablaAcSure");
                        String yukariYavasLimit = machineObj.getString("yukariYavasLimit");
                        String devirmeYukariIleriLimit = machineObj.getString("devirmeYukariIleriLimit");
                        String devirmeAsagiGeriLimit = machineObj.getString("devirmeAsagiGeriLimit");
                        String devirmeSilindirTipi = machineObj.getString("devirmeSilindirTipi");
                        String platformSilindirTipi = machineObj.getString("platformSilindirTipi");
                        String yukariValfTmr = machineObj.getString("yukariValfTmr");
                        String asagiValfTmr = machineObj.getString("asagiValfTmr");
                        String devirmeYukariIleriTmr = machineObj.getString("devirmeYukariIleriTmr");
                        String devirmeAsagiGeriTmr = machineObj.getString("devirmeAsagiGeriTmr");
                        String makineCalismaTmr = machineObj.getString("makineCalismaTmr");
                        String buzzer = machineObj.getString("buzzer");
                        String demoMode = machineObj.getString("demoMode");
                        String calismaSayisi1 = machineObj.getString("calismaSayisi1");
                        String calismaSayisi10 = machineObj.getString("calismaSayisi10");
                        String calismaSayisi100 = machineObj.getString("calismaSayisi100");
                        String calismaSayisi1000 = machineObj.getString("calismaSayisi1000");
                        String calismaSayisi10000 = machineObj.getString("calismaSayisi10000");
                        String dilSecim = machineObj.getString("dilSecim");
                        String eepromData37 = machineObj.getString("eepromData37");
                        String eepromData38 = machineObj.getString("eepromData38");
                        String eepromData39 = machineObj.getString("eepromData39");
                        String eepromData40 = machineObj.getString("eepromData40");
                        String eepromData41 = machineObj.getString("eepromData41");
                        String eepromData42 = machineObj.getString("eepromData42");
                        String eepromData43 = machineObj.getString("eepromData43");
                        String eepromData44 = machineObj.getString("eepromData44");
                        String eepromData45 = machineObj.getString("eepromData45");
                        String eepromData46 = machineObj.getString("eepromData46");
                        String eepromData47 = machineObj.getString("eepromData47");
                        String lcdBacklightSure = machineObj.getString("lcdBacklightSure");

                        Machine selectedMachine = new Machine(ownerUser, lastUpdate, machineType, machineID, devirmeYuruyusSecim, calismaSekli, emniyetCercevesi,
                                yavaslamaLimit, altLimit, kapiTablaAcKonum, basincSalteri, kapiSecimleri,
                                kapiAcTipi, kapi1Tip, kapi1AcSure, kapi2Tip, kapi2AcSure, kapitablaTip,
                                kapiTablaAcSure, yukariYavasLimit, devirmeYukariIleriLimit, devirmeAsagiGeriLimit,
                                devirmeSilindirTipi, platformSilindirTipi, yukariValfTmr, asagiValfTmr,
                                devirmeYukariIleriTmr, devirmeAsagiGeriTmr, makineCalismaTmr, buzzer, demoMode,
                                calismaSayisi1, calismaSayisi10, calismaSayisi100, calismaSayisi1000, calismaSayisi10000,
                                dilSecim, eepromData37, eepromData38, eepromData39, eepromData40, eepromData41,
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
        });
        return machines;
    }

    private void updateListView(ArrayList<Machine> machines) {
        machineList = machines;
        machineListAdapter = new MachineAdapter(getApplicationContext(), machineList);
        machineListView.setAdapter(machineListAdapter);
    }
}
