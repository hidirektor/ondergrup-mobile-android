package me.t3sl4.ondergrup.UI.Screens.Settings;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.sigma.niceswitch.NiceSwitch;

import de.hdodenhof.circleimageview.CircleImageView;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.UI.Screens.Documents.DocumentsScreen;
import me.t3sl4.ondergrup.UI.Screens.Profile.EditProfileScreen;
import me.t3sl4.ondergrup.UI.Screens.SupportTicket.SupportTickets;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import me.t3sl4.ondergrup.Util.HTTP.Requests.Auth.AuthService;
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

    private Dialog uyariDialog;
    private Dialog securityDialog;
    private Dialog changePassDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_dashboard);

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        uyariDialog = new Dialog(this);
        securityDialog = new Dialog(this);
        changePassDialog = new Dialog(this);

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
        Glide.with(this)
                .load(HttpHelper.BASE_URL + "/api/v2/user/getProfilePhoto/" + receivedUser.getUserName())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
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

        securityConstraint.setOnClickListener(v -> {
            openSecurityDialog();
        });
    }

    private void openSecurityDialog() {
        securityDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        securityDialog.setContentView(R.layout.bottom_sheet_security);

        LinearLayout changePass = securityDialog.findViewById(R.id.changePassLinear);
        LinearLayout loginLogs = securityDialog.findViewById(R.id.loginLogsLinear);
        LinearLayout multiFactor = securityDialog.findViewById(R.id.multiFactorLinear);

        changePass.setOnClickListener(v -> {
            securityDialog.dismiss();
            openChangePassDialog();
        });

        securityDialog.show();
        securityDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        securityDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        securityDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        securityDialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void openChangePassDialog() {
        changePassDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        changePassDialog.setContentView(R.layout.bottom_sheet_change_pass);

        EditText oldPasswordEditText = changePassDialog.findViewById(R.id.oldPasswordEditText);
        EditText newPasswordEditText = changePassDialog.findViewById(R.id.newPasswordEditText);
        NiceSwitch closeSessionsSwitch = changePassDialog.findViewById(R.id.closeSessionsSwitch);
        Button changePassButton = changePassDialog.findViewById(R.id.changePassButton);

        changePassButton.setOnClickListener(v -> {
            String oldPassword = oldPasswordEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();
            boolean closeSessions = closeSessionsSwitch.isChecked();

            AuthService.changePass(SettingsDashboard.this, UserDataService.getUserName(this), oldPassword, newPassword, closeSessions, () -> {
                changePassDialog.dismiss();
                Util.showSuccessPopup(uyariDialog, getString(R.string.password_changed));
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    uyariDialog.dismiss();
                }, 1000);
                new Handler(Looper.getMainLooper()).postDelayed(() -> {
                    securityDialog.show();
                }, 500);
            });
        });

        changePassDialog.show();
        changePassDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        changePassDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        changePassDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        changePassDialog.getWindow().setGravity(Gravity.BOTTOM);
    }
}
