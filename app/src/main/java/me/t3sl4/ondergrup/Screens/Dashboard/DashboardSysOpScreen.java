package me.t3sl4.ondergrup.Screens.Dashboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.Util;

public class DashboardSysOpScreen extends AppCompatActivity {
    public Util util;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sysop);

        util = new Util(getApplicationContext());
    }
}
