package me.t3sl4.ondergrup.Screens.Settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Documents.DocumentsScreen;
import me.t3sl4.ondergrup.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.Screens.SupportTicket.SupportTickets;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import me.t3sl4.ondergrup.Util.Util;

public class SettingsDashboard extends AppCompatActivity {
    public User receivedUser;

    private ImageView backButton;

    private ImageView editProfileButton;
    private CircleImageView profileImage;
    private TextView nameSurname;
    private TextView eMail;
    private TextView phoneNumber;

    private TextView currentLang;
    private ImageView changeLanguage;

    private ConstraintLayout notificationsConstraint;
    private ConstraintLayout privacyConstraint;
    private ConstraintLayout securityConstraint;
    private ConstraintLayout fraudConstraint;
    private ConstraintLayout helpConstraint;
    private ConstraintLayout aboutConstraint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_dashboard);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        componentInitialize();

        setUserInfo();

        buttonClickListeners();
    }

    private void componentInitialize() {
        backButton = findViewById(R.id.backButton);

        editProfileButton = findViewById(R.id.editProfileButton);
        profileImage = findViewById(R.id.profileImage);
        nameSurname = findViewById(R.id.userNameSurname);
        eMail = findViewById(R.id.userEmail);
        phoneNumber = findViewById(R.id.userPhoneNumber);

        currentLang = findViewById(R.id.currentLang);
        changeLanguage = findViewById(R.id.changeLanguageButton);

        notificationsConstraint = findViewById(R.id.notificationsButton);
        privacyConstraint = findViewById(R.id.privacyButton);
        securityConstraint = findViewById(R.id.securityButton);
        fraudConstraint = findViewById(R.id.fraudButton);
        helpConstraint = findViewById(R.id.helpButton);
        aboutConstraint = findViewById(R.id.aboutButton);
    }

    private void setUserInfo() {
        Log.d("USERNAME: ", receivedUser.getUserName().toString());
        Glide.with(this)
                .load(HttpHelper.BASE_URL + "/api/v2/user/getProfilePhoto/" + receivedUser.getUserName())
                .error(R.drawable.ikon_blank_profile_photo)
                .into(profileImage);
        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        phoneNumber.setText(receivedUser.getPhoneNumber());

        currentLang.setText(this.getResources().getString(R.string.active_language) + " " + this.getResources().getString(R.string.lang_turkish));
    }

    private void buttonClickListeners() {
        backButton.setOnClickListener(v -> finish());

        editProfileButton.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(SettingsDashboard.this, EditProfileScreen.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });

        changeLanguage.setOnClickListener(v -> {
            Util.changeSystemLanguage(this);
            recreate();
        });

        aboutConstraint.setOnClickListener(v -> {
            Intent belgelerIntent = new Intent(SettingsDashboard.this, DocumentsScreen.class);
            startActivity(belgelerIntent);
        });

        helpConstraint.setOnClickListener(v -> {
            Intent settingsIntent = new Intent(SettingsDashboard.this, SupportTickets.class);
            settingsIntent.putExtra("user", receivedUser);
            startActivity(settingsIntent);
        });
    }
}
