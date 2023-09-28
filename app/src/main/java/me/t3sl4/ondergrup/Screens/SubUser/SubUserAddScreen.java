package me.t3sl4.ondergrup.Screens.SubUser;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.HTTP.HTTP;
import me.t3sl4.ondergrup.Util.HTTP.VolleyMultipartRequest;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class SubUserAddScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ImageView signUp;
    private ImageView profilePhoto;
    private EditText editTextNickname;
    private EditText editTextMail;
    private EditText editTextPassword;
    private EditText editTextNameSurname;
    private EditText editTextPhone;
    private EditText editTextCompany;
    boolean isPhotoSelected = false;
    private Uri selectedImageUri;
    private boolean isPasswordVisible = false;

    private Dialog uyariDiyalog;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_subuser_add);

        uyariDiyalog = new Dialog(this);

        util = new Util(getApplicationContext());
        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        editTextNickname = findViewById(R.id.editTextNickname);
        editTextMail = findViewById(R.id.editTextMail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextNameSurname = findViewById(R.id.editTextNameSurname);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextCompany = findViewById(R.id.editTextCompany);

        profilePhoto = findViewById(R.id.profilePhoto);

        signUp = findViewById(R.id.signUp);
        signUp.setOnClickListener(v -> sendRegisterRequest());
        signUp.setOnTouchListener((v, event) -> {

            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                    view.invalidate();
                    break;
                }
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL: {
                    ImageView view = (ImageView) v;
                    view.getDrawable().clearColorFilter();
                    view.invalidate();
                    break;
                }
            }

            return false;
        });

        profilePhoto.setOnClickListener(v -> {
            Intent photoIntent = new Intent(Intent.ACTION_PICK);
            photoIntent.setType("image/*");
            startActivityForResult(photoIntent, 1);
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
                                "\"OwnerName\":\"" + receivedUser.getUserName() + "\"," +
                                "\"Created_At\":\"" + createdAt + "\"" +
                                "}";

                sendRegisterRequestFinal(registerJsonBody, userName);
            } else {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcı eklemek için profil fotoğrafı da seçmelisin.");
            }
        } else {
            util.showErrorPopup(uyariDiyalog, "Alt kullanıcı eklemek için tüm alanları doldurmalısın.");
        }
    }

    private void sendRegisterRequestFinal(String jsonBody, String userName) {
        String registerUrl = util.BASE_URL + util.addSubURLPrefix;

        HTTP http = new HTTP(this);
        http.sendRequest(registerUrl, jsonBody, new HTTP.HttpRequestCallback() {
            @Override
            public void onSuccess(JSONObject response) {
                uploadProfilePhoto2Server(userName);
            }

            @Override
            public void onFailure(String errorMessage) {
                util.showErrorPopup(uyariDiyalog, "Alt kullanıcı eklerken hata meydana geldi. Lütfen tekrar dene.");
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
