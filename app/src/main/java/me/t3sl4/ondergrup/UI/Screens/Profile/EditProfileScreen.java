package me.t3sl4.ondergrup.UI.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import de.hdodenhof.circleimageview.CircleImageView;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.HttpHelper;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;

public class EditProfileScreen extends AppCompatActivity {
    public User receivedUser;
    public User mainUser;

    public String incomeScreen;

    private ImageView backButton;

    private CircleImageView profileImage;
    private EditText nameSurname;
    private EditText eMail;
    private EditText kullaniciAdi;
    private EditText phone;
    private EditText companyName;
    private Button showProfile;
    private Button updateProfileButton;

    private Dialog uyariDiyalog;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_edit);

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        mainUser = intent.getParcelableExtra("mainUser");
        incomeScreen = intent.getStringExtra("incomeScreen");

        initializeComponents();

        if(mainUser != null) {
            showProfile.setVisibility(View.GONE);
        }

        if(receivedUser.getOwnerName() != null) {
            companyName.setInputType(InputType.TYPE_NULL);
        }

        buttonClickListeners();

        setUserInfo();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleBackPress();
            }
        });
    }

    private void initializeComponents() {
        backButton = findViewById(R.id.backButton);

        profileImage = findViewById(R.id.profileImage);
        nameSurname = findViewById(R.id.editTextNameSurname);
        eMail = findViewById(R.id.editTextMail);
        kullaniciAdi = findViewById(R.id.editTextNickname);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.editTextCompany);

        showProfile = findViewById(R.id.showProfile);
        updateProfileButton = findViewById(R.id.button3);
    }

    private void buttonClickListeners() {
        showProfile.setOnClickListener(v -> {
            Intent profileIntent = new Intent(EditProfileScreen.this, ProfileScreen.class);
            if(mainUser != null) {
                profileIntent.putExtra("user", mainUser);
            } else {
                profileIntent.putExtra("user", receivedUser);
            }

            startActivity(profileIntent);
            finish();
        });

        updateProfileButton.setOnClickListener(v -> {
            updateWholeProfile();
        });

        backButton.setOnClickListener(v -> finish());

        profileImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_IMAGE_REQUEST);
        });
    }

    public void setUserInfo() {
        Glide.with(this)
                .load(HttpHelper.BASE_URL + "/api/v2/user/getProfilePhoto/" + receivedUser.getUserName())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ikon_blank_profile_photo)
                .into(profileImage);

        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        kullaniciAdi.setText(receivedUser.getUserName());
        phone.setText(receivedUser.getPhoneNumber());
        companyName.setText(receivedUser.getCompanyName());
    }

    public void updateWholeProfile() {
        UserService.updateProfile(this, UserDataService.getUserID(this), nameSurname.getText().toString(), eMail.getText().toString(), companyName.getText().toString(), phone.getText().toString(), () -> {
            finish();
            updateUserObject(receivedUser);
            startActivity(getIntent().putExtra("user", receivedUser));
        });
    }

    private void handleBackPress() {
        Intent profileIntent = null;
        if(incomeScreen == null) {
            profileIntent = new Intent(EditProfileScreen.this, ProfileScreen.class);
            profileIntent.putExtra("user", receivedUser);
            startActivity(profileIntent);
        }

        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            try {
                Glide.with(this).load(selectedImageUri).into(profileImage);

                uploadPhoto(selectedImageUri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void uploadPhoto(Uri imageUri) {
        try {
            InputStream inputStream = getContentResolver().openInputStream(imageUri);

            if (inputStream != null) {
                File photoFile = new File(getCacheDir(), "profile_photo.jpg");
                OutputStream outputStream = new FileOutputStream(photoFile);

                byte[] buffer = new byte[1024];
                int length;
                while ((length = inputStream.read(buffer)) > 0) {
                    outputStream.write(buffer, 0, length);
                }

                outputStream.close();
                inputStream.close();

                UserService.uploadProfilePhoto(this, receivedUser.getUserName(), photoFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateUserObject(User receivedUser) {
        receivedUser.setNameSurname(String.valueOf(nameSurname.getText()));
        receivedUser.seteMail(String.valueOf(eMail.getText()));
        receivedUser.setPhoneNumber(String.valueOf(phone.getText()));
        receivedUser.setCompanyName(String.valueOf(companyName.getText()));
    }

    public String getRealPathFromURI(Uri uri) {
        String result;
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        if (cursor == null) {
            result = uri.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }
}
