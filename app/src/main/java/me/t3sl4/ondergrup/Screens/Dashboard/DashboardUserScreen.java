package me.t3sl4.ondergrup.Screens.Dashboard;

import android.app.Dialog;
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
import me.t3sl4.ondergrup.Screens.Documents.DocumentsScreen;
import me.t3sl4.ondergrup.Screens.Machine.MachineListScreen;
import me.t3sl4.ondergrup.Screens.MainActivity;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserScreen;
import me.t3sl4.ondergrup.Screens.Support.SupportScreen;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class DashboardUserScreen extends AppCompatActivity {
    public Util util;

    private TextView isimSoyisim;
    private ImageView profilePhotoView;

    private ConstraintLayout profileButton;
    private LinearLayout destekButton;
    private ConstraintLayout settingsButton;
    private ConstraintLayout belgelerButton;
    private ConstraintLayout subUserButton;
    private ConstraintLayout machineManageButton;

    public User receivedUser;

    private Dialog uyariDiyalog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user);

        util = new Util(getApplicationContext());
        uyariDiyalog = new Dialog(this);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        isimSoyisim = findViewById(R.id.textView4);
        profilePhotoView = findViewById(R.id.imageView4);

        profileButton = findViewById(R.id.profileConstraint);
        destekButton = findViewById(R.id.destekButton);
        settingsButton = findViewById(R.id.settingsConstraint);
        belgelerButton = findViewById(R.id.belgelerConstraint);
        subUserButton = findViewById(R.id.subUserConstraint);
        machineManageButton = findViewById(R.id.machineManageConstraint);

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(DashboardUserScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", util.user);
            startActivity(profileIntent);
            finish();
        });

        destekButton.setOnClickListener(v -> {
            Intent destekIntent = new Intent(DashboardUserScreen.this, SupportScreen.class);
            destekIntent.putExtra("user", util.user);
            startActivity(destekIntent);
        });

        settingsButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(DashboardUserScreen.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
            finish();
        });

        belgelerButton.setOnClickListener(v -> {
            Intent belgelerIntent = new Intent(DashboardUserScreen.this, DocumentsScreen.class);
            startActivity(belgelerIntent);
        });

        subUserButton.setOnClickListener(v -> {
            if(receivedUser.getOwnerName() != null) {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcıları yalnızca yöneticiniz görüntüleyebilir.");
            } else {
                Intent settingsIntent = new Intent(DashboardUserScreen.this, SubUserScreen.class);
                settingsIntent.putExtra("user", receivedUser);
                startActivity(settingsIntent);
            }
        });

        machineManageButton.setOnClickListener(v -> {
           Intent manageMachineIntent = new Intent(DashboardUserScreen.this, MachineListScreen.class);
           manageMachineIntent.putExtra("user", receivedUser);
           startActivity(manageMachineIntent);
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