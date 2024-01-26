package me.t3sl4.ondergrup.Screens.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class ProfileScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ImageView backButton;

    private TextView nameSurname;
    private TextView eMail;
    private TextView kullaniciRolu;
    private TextView kullaniciAdi;
    private TextView phone;
    private TextView companyName;
    private TextView createdDate;
    private Button editProfileButton;
    private LinearLayout subUserButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        backButton = findViewById(R.id.backButton);

        nameSurname = findViewById(R.id.textView);
        eMail = findViewById(R.id.textView2);
        kullaniciRolu = findViewById(R.id.textView18);
        kullaniciAdi = findViewById(R.id.textView20);
        phone = findViewById(R.id.textView22);
        companyName = findViewById(R.id.textView24);
        createdDate = findViewById(R.id.textView27);

        editProfileButton = findViewById(R.id.button3);
        subUserButton = findViewById(R.id.subUserConstraint);

        editProfileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(ProfileScreen.this, EditProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
            finish();
        });

        subUserButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(ProfileScreen.this, SubUserScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });

        backButton.setOnClickListener(v -> finish());

        setUserInfo();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        String userRole = receivedUser.getRole();
        Log.d("Rol", " " + userRole);
        if (userRole.equals("NORMAL")) {
            Intent profileIntent = new Intent(ProfileScreen.this, me.t3sl4.ondergrup.Screens.Dashboard.User.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        } /*else if(userRole.equals("TECHNICIAN")) {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardTechnicianScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        } else if(userRole.equals("ENGINEER")) {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardEngineerScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        } else if(userRole.equals("SYSOP")) {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardSysOpScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        }*/
    }

    public void setUserInfo() {
        String createdAtString = receivedUser.getCreatedAt();

        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        kullaniciRolu.setText(receivedUser.getRole());
        kullaniciAdi.setText(receivedUser.getUserName());
        phone.setText(receivedUser.getPhoneNumber());
        companyName.setText(receivedUser.getCompanyName());
        createdDate.setText(Util.dateTimeConvert(createdAtString));
    }

}
