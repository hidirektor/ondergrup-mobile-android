package me.t3sl4.ondergrup.Screens.SubUser;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardUserScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.Profile.ProfileScreen;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private Button subUserListeleButton;
    private Button subUsterEkleButton;

    private ConstraintLayout homeButton;
    private ConstraintLayout profileButton;
    private ConstraintLayout machineButton;
    private ConstraintLayout settingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        subUserListeleButton = findViewById(R.id.showButton);
        subUsterEkleButton = findViewById(R.id.addSubUserButton);

        homeButton = findViewById(R.id.mainConstraint);
        profileButton = findViewById(R.id.profileConstraint);
        machineButton = findViewById(R.id.machineConstraint);
        settingsButton = findViewById(R.id.settingsConstraint);

        subUserListeleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserListScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        subUsterEkleButton.setOnClickListener(v -> {
            Intent addIntent = new Intent(SubUserScreen.this, SubUserAddScreen.class);
            addIntent.putExtra("user", receivedUser);
            startActivity(addIntent);
        });

        homeButton.setOnClickListener(v -> {
            Intent homeIntent = new Intent(SubUserScreen.this, DashboardUserScreen.class);
            homeIntent.putExtra("user", receivedUser);
            startActivity(homeIntent);
            finish();
        });

        profileButton.setOnClickListener(v -> {
            Intent profileIntent = new Intent(SubUserScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
            finish();
        });

        machineButton.setOnClickListener(v -> {
            //TODO
            //Makinelerim ekranı
        });

        settingsButton.setOnClickListener(v -> {
            Intent editProfileIntent = new Intent(SubUserScreen.this, EditProfileScreen.class);
            editProfileIntent.putExtra("user", receivedUser);
            startActivity(editProfileIntent);
        });
    }
}
