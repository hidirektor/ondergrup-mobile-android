package me.t3sl4.ondergrup.Screens.Auth;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import me.t3sl4.ondergrup.R;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class RegisterScreen extends AppCompatActivity {

    private ImageView imageView2;
    private ImageView profilePhoto;
    private EditText editTextTextPersonName3;
    private EditText editTextTextPersonName4;
    private EditText editTextTextPassword;
    private EditText editTextTextPersonName;
    private EditText editTextTextPasswordName3;
    private EditText editTextTextPasswordName2;
    boolean isPhotoSelected = false;
    private Uri selectedImageUri;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextTextPersonName3 = findViewById(R.id.editTextTextPersonName3);
        editTextTextPersonName4 = findViewById(R.id.editTextTextPersonName4);
        editTextTextPassword = findViewById(R.id.editTextTextPassword);
        editTextTextPersonName = findViewById(R.id.editTextTextPersonName);
        editTextTextPasswordName3 = findViewById(R.id.editTextTextPasswordName3);
        editTextTextPasswordName2 = findViewById(R.id.editTextTextPasswordName2);

        profilePhoto = findViewById(R.id.profilePhoto);

        imageView2 = findViewById(R.id.imageView2);
        imageView2.setOnClickListener(v -> sendRegisterRequest());

        profilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        editTextTextPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextTextPassword.getRight() - editTextTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        togglePasswordVisibility();
                        return true;
                    }
                }
                return false;
            }
        });
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
        editTextTextPassword.setInputType(inputType);
        editTextTextPassword.setSelection(editTextTextPassword.getText().length());
    }

    private void updatePasswordToggleIcon(@DrawableRes int drawableResId) {
        Drawable[] drawables = editTextTextPassword.getCompoundDrawablesRelative();
        drawables[2] = getResources().getDrawable(drawableResId, getApplicationContext().getTheme());
        editTextTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Store the selected image URI
            isPhotoSelected = true;

            // Set the selected image to the profilePhoto ImageView
            profilePhoto.setImageURI(selectedImageUri);
        }
    }

    private void sendRegisterRequest() {
        String userName = editTextTextPersonName3.getText().toString();
        String email = editTextTextPersonName4.getText().toString();
        String password = editTextTextPassword.getText().toString();
        String nameSurname = editTextTextPersonName.getText().toString();
        String phone = editTextTextPasswordName3.getText().toString();
        String companyName = editTextTextPasswordName2.getText().toString();
        String filePath;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String createdAt = sdf.format(new Date());

        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("Role", "NORMAL")
                .addFormDataPart("UserName", userName)
                .addFormDataPart("Email", email)
                .addFormDataPart("Password", password)
                .addFormDataPart("NameSurname", nameSurname)
                .addFormDataPart("Phone", phone)
                .addFormDataPart("Profile_Photo", "C:/imgs/test.png")
                .addFormDataPart("CompanyName", companyName)
                .addFormDataPart("Created_At", createdAt);

        if (isPhotoSelected) {
            filePath = getRealPathFromURI(selectedImageUri);
            if (filePath != null) {
                File photoFile = new File(filePath);
                builder.addFormDataPart("Profile_Photo", "profile_photo.png", RequestBody.create(MediaType.parse("image/*"), photoFile));
            } else {
                Log.d("RegisterScreen", "File path is null.");
            }
        } else {
            filePath = "";
        }

        RequestBody requestBody = builder.build();

        /*HTTPRequest.postPhoto("http://85.95.231.92:3000/api/register", requestBody, response -> {
            Log.d("RegisterScreen", "Register request response: " + response);

            if (isPhotoSelected) {
                String username = userName;
                uploadProfilePhoto(username, filePath);
            }
        });*/
    }

    private void uploadProfilePhoto(String username, String filePath) {
        File photoFile = new File(filePath);
        if (!photoFile.exists()) {
            Log.d("UploadPhoto", "File does not exist: " + filePath);
            return;
        }

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("file", "profile_photo.png", RequestBody.create(MediaType.parse("image/*"), photoFile))
                .build();

        /*HTTPRequest.postPhoto("http://85.95.231.92:3000/api/fileSystem/upload", requestBody, response -> {
            Log.d("UploadPhoto", "Upload response: " + response);
        });*/
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(contentUri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(column_index);
        cursor.close();
        return filePath;
    }
}
