package me.t3sl4.ondergrup.Screens.Auth;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.ResetPassword.ForgetPassword;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.HTTP.VolleyMultipartRequest;
import me.t3sl4.ondergrup.Util.Util;

public class RegisterScreen extends AppCompatActivity {

    private ImageView signUp;
    private ImageView profilePhoto;
    private EditText editTextNickname;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextNameSurname;
    private EditText editTextPhone;
    private EditText editTextCompany;
    private TextView resetPass;
    boolean isPhotoSelected = false;
    private Uri selectedImageUri;
    private boolean isPasswordVisible = false;

    public Util util;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        util = new Util(getApplicationContext());

        uyariDiyalog = new Dialog(this);

        editTextNickname = findViewById(R.id.editTextNickname);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNameSurname = findViewById(R.id.editTextNameSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCompany = findViewById(R.id.editTextCompany);
        resetPass = findViewById(R.id.resetPass);

        profilePhoto = findViewById(R.id.profilePhoto);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(v -> sendRegisterRequest());

        profilePhoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 1);
        });

        resetPass.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterScreen.this, ForgetPassword.class);
            startActivity(intent);
        });

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
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
        editTextPassword.setInputType(inputType);
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private void updatePasswordToggleIcon(@DrawableRes int drawableResId) {
        Drawable[] drawables = editTextPassword.getCompoundDrawablesRelative();
        drawables[2] = getResources().getDrawable(drawableResId, getApplicationContext().getTheme());
        editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2], drawables[3]);
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

    private void sendRegisterRequest() {
        String userName = editTextNickname.getText().toString();
        String email = editTextMail.getText().toString();
        String password = editTextPassword.getText().toString();
        String nameSurname = editTextNameSurname.getText().toString();
        String phone = editTextPhone.getText().toString();
        String companyName = editTextCompany.getText().toString();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String createdAt = sdf.format(new Date());

        if(checkFields(userName, email, password, nameSurname, phone, companyName)) {
            if(isPhotoSelected) {
                String profilePhotoPath = userName + ".jpg";
                String registerJsonBody =
                        "{" +
                                "\"Role\":\"" + "NORMAL" + "\"," +
                                "\"UserName\":\"" + userName + "\"," +
                                "\"Email\":\"" + email + "\"," +
                                "\"Password\":\"" + password + "\"," +
                                "\"NameSurname\":\"" + nameSurname + "\"," +
                                "\"Phone\":\"" + phone + "\"," +
                                "\"Profile_Photo\":\"" + profilePhotoPath + "\"," +
                                "\"CompanyName\":\"" + companyName + "\"," +
                                "\"Created_At\":\"" + createdAt + "\"" +
                                "}";

                sendRegisterRequest(registerJsonBody, userName);
            } else {
                util.showErrorPopup(uyariDiyalog, "Kayıt olmak için profil fotoğrafı da seçmelisin.");
            }
        } else {
            util.showErrorPopup(uyariDiyalog, "Kayıt olmak için tüm alanları doldurmalısın.");
        }
    }

    private void sendRegisterRequest(String jsonBody, String userName) {
        String registerUrl = util.BASE_URL + util.registerURLPrefix;

        HTTP http = new HTTP(this);
        http.sendRequest(registerUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) throws IOException {
                uploadProfilePhoto2Server(userName);
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Kayıt olurken hata meydana geldi. Lütfen tekrar dene.");
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
                    Intent intent = new Intent(RegisterScreen.this, LoginScreen.class);
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

    private boolean checkFields(String userName, String email, String password, String nameSurname, String phone, String companyName) {
        return !userName.isEmpty() && !email.isEmpty() && !password.isEmpty() && !nameSurname.isEmpty() && !phone.isEmpty() && !companyName.isEmpty();
    }
}
