package me.t3sl4.ondergrup.Screens.Machine;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.Machine.Adapter.MachineAdapter;
import me.t3sl4.ondergrup.Model.Machine.Machine;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPMachineService;

public class MachineListScreen extends AppCompatActivity {
    public User receivedUser;

    private ImageView backButton;

    private ListView machineListView;
    private MachineAdapter machineListAdapter;
    private ArrayList<Machine> machineList;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_machine_list);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        backButton = findViewById(R.id.backButton);

        machineListView = findViewById(R.id.machineListView);
        machineList = getMachineList();

        machineListView.setOnItemClickListener((parent, view, position, id) -> {
            Machine selectedMachine = machineList.get(position);

            Intent machineIntent = new Intent(MachineListScreen.this, MachineScreen.class);
            machineIntent.putExtra("machine", selectedMachine);
            machineIntent.putExtra("user", receivedUser);
            startActivity(machineIntent);
        });

        backButton.setOnClickListener(v -> finish());
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
        machineListAdapter = new MachineAdapter(getApplicationContext(), machineList);
        machineListView.setAdapter(machineListAdapter);
    }
}
