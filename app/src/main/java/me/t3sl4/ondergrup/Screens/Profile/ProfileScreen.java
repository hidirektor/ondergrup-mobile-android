package me.t3sl4.ondergrup.Screens.Profile;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardEngineerScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardSysOpScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardTechnicianScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class ProfileScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ImageView profilePhoto;
    private TextView nameSurname;
    private TextView eMail;
    private TextView kullaniciRolu;
    private TextView kullaniciAdi;
    private TextView phone;
    private TextView companyName;
    private TextView createdDate;
    private Button editProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        profilePhoto = findViewById(R.id.imageView);
        nameSurname = findViewById(R.id.textView);
        eMail = findViewById(R.id.textView2);
        kullaniciRolu = findViewById(R.id.textView18);
        kullaniciAdi = findViewById(R.id.textView20);
        phone = findViewById(R.id.textView22);
        companyName = findViewById(R.id.textView24);
        createdDate = findViewById(R.id.textView27);

        editProfileButton = findViewById(R.id.button3);

        editProfileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(ProfileScreen.this, EditProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
            finish();
        });

        setUserInfo();
    }

    @Override
    public void onBackPressed() {
        String userRole = receivedUser.getRole();
        if(userRole == "NORMAL") {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardUserScreen.class);
            finish();
            startActivity(profileIntent.putExtra("user", receivedUser));
        } else if(userRole == "TECHNICIAN") {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardTechnicianScreen.class);
            finish();
            startActivity(profileIntent.putExtra("user", receivedUser));
        } else if(userRole == "ENGINEER") {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardEngineerScreen.class);
            finish();
            startActivity(profileIntent.putExtra("user", receivedUser));
        } else {
            Intent profileIntent = new Intent(ProfileScreen.this, DashboardSysOpScreen.class);
            finish();
            startActivity(profileIntent.putExtra("user", receivedUser));
        }
    }

    public void setUserInfo() {
        String imageUrl = util.BASE_URL + util.getPhotoURLPrefix + receivedUser.getUserName() + ".jpg";

        String createdAtString = receivedUser.getCreatedAt();

        Glide.with(this).load(imageUrl).override(100, 100).into(profilePhoto);
        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        kullaniciRolu.setText(receivedUser.getRole());
        kullaniciAdi.setText(receivedUser.getUserName());
        phone.setText(receivedUser.getPhoneNumber());
        companyName.setText(receivedUser.getCompanyName());
        createdDate.setText(Util.dateTimeConvert(createdAtString));
    }

}
