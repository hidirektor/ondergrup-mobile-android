package me.t3sl4.ondergrup.Screens.Support;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SupportScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private Button mapButton;
    private Button mailButton;
    private Button callButton;

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

        mapButton = findViewById(R.id.mapButton);
        mailButton = findViewById(R.id.mailButton);
        callButton = findViewById(R.id.callButton);

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
            Intent mainIntent = new Intent(SupportScreen.this, me.t3sl4.ondergrup.Screens.Dashboard.User.class);
            startActivity(mainIntent);
            finish();
        } /*else if(role == "TECHNICIAN") {
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
        }*/
    }
}
