package me.t3sl4.ondergrup.Screens.Dashboard;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import java.io.File;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.MainActivity;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.Support.SupportScreen;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class DashboardTechnicianScreen extends AppCompatActivity {
    public Util util;

    private TextView isimSoyisim;
    private ImageView profilePhotoView;

    private ConstraintLayout profileButton;
    private ConstraintLayout settingsButton;
    private LinearLayout destekButton;

    public User receivedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_technician);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        isimSoyisim = findViewById(R.id.textView4);
        profilePhotoView = findViewById(R.id.imageView4);

        profileButton = findViewById(R.id.profileConstraint);
        destekButton = findViewById(R.id.destekButton);
        settingsButton = findViewById(R.id.settingsConstraint);

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(DashboardTechnicianScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
            finish();
        });

        destekButton.setOnClickListener(v -> {
            Intent destekIntent = new Intent(DashboardTechnicianScreen.this, SupportScreen.class);
            destekIntent.putExtra("user", util.user);
            startActivity(destekIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(DashboardTechnicianScreen.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });

        setUserInfo();
    }

    public void setUserInfo() {
        isimSoyisim.setText(receivedUser.getNameSurname());
        String imageUrl = util.BASE_URL + util.getPhotoURLPrefix + receivedUser.getUserName() + ".jpg";
        Glide.with(this)
                .load(imageUrl).override(100,100)
                .into(profilePhotoView);

    }
}
