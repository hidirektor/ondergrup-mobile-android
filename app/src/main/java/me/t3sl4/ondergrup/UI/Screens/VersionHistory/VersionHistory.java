package me.t3sl4.ondergrup.UI.Screens.VersionHistory;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import me.t3sl4.ondergrup.Model.MachineVersion.Adapter.VersionAdapter;
import me.t3sl4.ondergrup.Model.MachineVersion.Version;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Authorized.OPVersionService;
import me.t3sl4.ondergrup.Util.Util;

public class VersionHistory extends AppCompatActivity {
    public User receivedUser;

    private Dialog uyariDiyalog;

    private ImageView backToMainButton;

    private ListView versionHistoryList;
    private VersionAdapter machineVersionAdapter;
    private ArrayList<Version> machineVersionList;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_version_history);

        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        initializeComponents();
    }

    private void initializeComponents() {
        backToMainButton = findViewById(R.id.backToMain);

        versionHistoryList = findViewById(R.id.versionHistoryList);
        machineVersionList = getMachineVersionList();
    }

    private ArrayList<Version> getMachineVersionList() {
        ArrayList<Version> machineVersionsTemp = new ArrayList<>();

        OPVersionService.getAllVersions(this, machineVersionsTemp, () -> {
            updateListView(machineVersionsTemp);
        }, () -> {
            String hataMesaj = this.getResources().getString(R.string.version_not_found);
            Util.showErrorPopup(uyariDiyalog, hataMesaj);

            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                uyariDiyalog.dismiss();
                finish();
            }, 1000);
        });

        return machineVersionsTemp;
    }

    private void updateListView(ArrayList<Version> machineVersionsTemp) {
        machineVersionList = machineVersionsTemp;
        machineVersionAdapter = new VersionAdapter(this, machineVersionList);
        versionHistoryList.setAdapter(machineVersionAdapter);
    }
}
