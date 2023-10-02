package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.DashboardTechnicianScreen;
import me.t3sl4.ondergrup.Screens.SubUser.Adapter.SubUser;
import me.t3sl4.ondergrup.Screens.SubUser.SubUserListScreen;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.HTTP.VolleyMultipartRequest;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class EditSubScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;
    public SubUser receivedSubUser;

    private ImageView profilePhoto;
    private EditText nameSurname;
    private EditText eMail;
    private EditText kullaniciAdi;
    private EditText phone;
    private EditText companyName;
    private EditText passwordEditText;
    private LinearLayout showSubUsers;
    private Button updateProfileButton;
    private ImageView uploadPhoto;

    private Dialog uyariDiyalog;

    private boolean isPasswordVisible = false;
    private boolean isPhotoSelected = false;
    private Uri selectedImageUri;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_edit);

        util = new Util(getApplicationContext());

        uyariDiyalog = new Dialog(this);
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");
        receivedSubUser = intent.getParcelableExtra("subuser");

        profilePhoto = findViewById(R.id.imageView);
        nameSurname = findViewById(R.id.editTextNameSurname);
        eMail = findViewById(R.id.editTextMail);
        kullaniciAdi = findViewById(R.id.editTextNickname);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.editTextCompany);
        passwordEditText = findViewById(R.id.editTextPassword);
        uploadPhoto = findViewById(R.id.imageViewSelect);

        showSubUsers = findViewById(R.id.showSubUsersLayout);
        updateProfileButton = findViewById(R.id.button3);

        showSubUsers.setOnClickListener(v -> {
            Intent subUserIntent = new Intent(EditSubScreen.this, SubUserListScreen.class);
            subUserIntent.putExtra("user", receivedUser);
            startActivity(subUserIntent);
            finish();
        });

        updateProfileButton.setOnClickListener(v -> {
            updateWholeProfile();
        });

        uploadPhoto.setOnClickListener(v -> {
            Intent photoPick = new Intent(Intent.ACTION_PICK);
            photoPick.setType("image/*");
            startActivityForResult(photoPick, 1);
        });

        passwordEditText.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (passwordEditText.getRight() - passwordEditText.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });

        setUserInfo();
    }

    public void setUserInfo() {
        String imageUrl = util.BASE_URL + util.getPhotoURLPrefix + receivedSubUser.getUserName() + ".jpg";
        String imageUrl2 = util.BASE_URL + util.getPhotoURLPrefix + receivedUser.getUserName() + ".png";

        Glide.with(this)
                .load(imageUrl)
                .override(100, 100)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        Glide.with(EditSubScreen.this)
                                .load(imageUrl2)
                                .override(100, 100)
                                .into(profilePhoto);
                        return true;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(profilePhoto);
        nameSurname.setText(receivedSubUser.getNameSurname());
        eMail.setText(receivedSubUser.geteMail());
        kullaniciAdi.setText(receivedSubUser.getUserName());
        phone.setText(receivedSubUser.getPhone());
        companyName.setText(receivedSubUser.getCompanyName());
    }

    public void updateWholeProfile() {
        String created_at = Util.getCurrentDateTime();
        String password = String.valueOf(passwordEditText.getText());

        if (password.isEmpty()) {
            password = "null";
        }

        String registerJsonBody =
                "{" +
                        "\"UserName\":\"" + kullaniciAdi.getText() + "\"," +
                        "\"Email\":\"" + eMail.getText() + "\"," +
                        "\"Password\":\"" + password + "\"," +
                        "\"NameSurname\":\"" + nameSurname.getText() + "\"," +
                        "\"Phone\":\"" + phone.getText() + "\"," +
                        "\"CompanyName\":\"" + companyName.getText() + "\"" +
                        "}";

        sendUpdateRequest(registerJsonBody, String.valueOf(kullaniciAdi.getText()));
    }

    private void sendUpdateRequest(String jsonBody, String username) {
        String updateProfileUrl = util.BASE_URL + util.updateProfileURLPrefix;

        HTTP http = new HTTP(this);
        http.sendRequest(updateProfileUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                if(selectedImageUri != null && isPhotoSelected) {
                    uploadProfilePhoto2Server(username);
                }
                finish();
                updateUserObject(receivedUser);
                startActivity(getIntent().putExtra("user", receivedUser));
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("Hata", " " + errorMessage);
                util.showErrorPopup(uyariDiyalog, "Profil güncellenirken hata meydana geldi. Lütfen tekrar dene.");
            }
        });
    }

    private void uploadProfilePhoto2Server(String userName) {
        String uploadUrl = util.BASE_URL + util.uploadURLPrefix;

        File profilePhotoFile = uriToFile(selectedImageUri);
        if (!profilePhotoFile.exists()) {
            util.showErrorPopup(uyariDiyalog, "Profil fotoğrafı bulunamadı. Lütfen tekrar dene.");
            return;
        }

        VolleyMultipartRequest multipartRequest = new VolleyMultipartRequest(
                Request.Method.POST,
                uploadUrl,
                response -> {
                    Intent intent = new Intent(EditSubScreen.this, LoginScreen.class);
                    startActivity(intent);
                    finish();
                },
                error -> {
                    util.showErrorPopup(uyariDiyalog, "Profil fotoğrafı yüklenirken hata meydana geldi. Lütfen tekrar dene.");
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", userName);
                return params;
            }

            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                String imageName = userName + ".jpg";
                profilePhoto.setDrawingCacheEnabled(true);
                profilePhoto.buildDrawingCache();
                Bitmap bitmap = profilePhoto.getDrawingCache();
                byte[] imageBytes = convertBitmapToByteArray(bitmap);
                params.put("file", new DataPart(imageName, imageBytes, "image/jpeg"));
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(multipartRequest);
    }

    @Override
    public void onBackPressed() {
        Intent profileIntent = new Intent(EditSubScreen.this, SubUserListScreen.class);
        finish();
        startActivity(profileIntent.putExtra("user", receivedUser));
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private File uriToFile(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();

        return new File(filePath);
    }

    public void updateUserObject(User receivedUser) {
        receivedUser.setNameSurname(String.valueOf(nameSurname.getText()));
        receivedUser.seteMail(String.valueOf(eMail.getText()));
        receivedUser.setPhoneNumber(String.valueOf(phone.getText()));
        receivedUser.setCompanyName(String.valueOf(companyName.getText()));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData();
            isPhotoSelected = true;

            profilePhoto.setImageURI(selectedImageUri);
        }
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;
        int drawableResId = isPasswordVisible ? R.drawable.field_password_hide : R.drawable.field_password_show;
        setPasswordVisibility(isPasswordVisible);
        updatePasswordToggleIcon(drawableResId);
    }

    private void setPasswordVisibility(boolean visible) {
        int inputType = visible ? android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                : android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
        passwordEditText.setInputType(inputType);
        passwordEditText.setSelection(passwordEditText.getText().length());
    }

    private void updatePasswordToggleIcon(@DrawableRes int drawableResId) {
        Drawable[] drawables = passwordEditText.getCompoundDrawablesRelative();
        drawables[2] = getResources().getDrawable(drawableResId, getApplicationContext().getTheme());
        passwordEditText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
