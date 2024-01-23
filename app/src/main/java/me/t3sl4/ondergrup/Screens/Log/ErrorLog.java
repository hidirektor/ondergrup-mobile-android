package me.t3sl4.ondergrup.Screens.Log;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Log.MachineError.MachineError;
import me.t3sl4.ondergrup.Screens.Log.MachineError.MachineErrorAdapter;
import me.t3sl4.ondergrup.Screens.Machine.Adapter.Machine;
import me.t3sl4.ondergrup.Screens.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class ErrorLog extends AppCompatActivity {
    public Util util;

    private LinearLayout backToMachine;

    private ListView machineErrors;
    private MachineErrorAdapter machineErrorAdapter;
    private ArrayList<MachineError> machineErrorList;

    private Dialog uyariDiyalog;

    String currentMachineID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_errorlog);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        if(intent != null) {
            currentMachineID = intent.getStringExtra("currentmachine");
        }

        backToMachine = findViewById(R.id.backToMachine);

        machineErrors = findViewById(R.id.machineErrors);
        machineErrorList = getMachineErrorList();

        machineErrors.setOnItemClickListener((parent, view, position, id) -> {
            MachineError selectedMachine = machineErrorList.get(position);

            //popup eklenecek

            /*Intent machineIntent = new Intent(ErrorLog.this, MachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);*/
        });

        backToMachine.setOnClickListener(v -> {
            finish();
        });
    }

    private ArrayList<MachineError> getMachineErrorList() {
        ArrayList<MachineError> machineErrorsTemp = new ArrayList<>();
        String reqURL = util.BASE_URL + util.getMachineErrorURL;
        String jsonErrorBody = "{\"machineID\": \"" + currentMachineID + "\"}";


        Log.d("ErrorLog-URL", reqURL);
        Log.d("ErrorLog-Body", jsonErrorBody);

        HTTP.sendRequest(reqURL, jsonErrorBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    JSONArray machineArray = response.getJSONArray("data");
                    Log.d("ErrorLog-data", String.valueOf(machineArray));
                    for (int i = 0; i < machineArray.length(); i++) {
                        JSONObject machineInfoObj = machineArray.getJSONObject(i);
                        String machineID = machineInfoObj.getString("MachineID");
                        String errorCode = machineInfoObj.getString("ErrorCode");
                        String errorDate = machineInfoObj.getString("ErrorDate");

                        MachineError selectedMachine = new MachineError(machineID, errorCode, errorDate);
                        machineErrorsTemp.add(selectedMachine);
                    }

                    updateListView(machineErrorsTemp);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(String errorMessage) {
                String hatasizMesaj = getApplicationContext().getResources().getString(R.string.hatayok);
                util.showSuccessPopup(uyariDiyalog, hatasizMesaj);
            }
        }, Volley.newRequestQueue(this));
        return machineErrorsTemp;
    }

    private void updateListView(ArrayList<MachineError> machineErrorsTemp) {
        machineErrorList = machineErrorsTemp;
        machineErrorAdapter = new MachineErrorAdapter(getApplicationContext(), machineErrorList);
        machineErrors.setAdapter(machineErrorAdapter);
    }
}
