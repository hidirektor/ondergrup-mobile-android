package me.t3sl4.ondergrup.Screens.Profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.User.User;
import me.t3sl4.ondergrup.Util.Util;

public class EditProfileScreen extends AppCompatActivity {
    public Util util;
    public User receivedUser;

    private ImageView profilePhoto;
    private EditText nameSurname;
    private EditText eMail;
    private EditText kullaniciAdi;
    private EditText phone;
    private EditText companyName;
    private EditText passwordEditText;

    private boolean isPasswordVisible = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_edit);

        util = new Util(getApplicationContext());

        Intent intent = getIntent();
        receivedUser = intent.getParcelableExtra("user");

        profilePhoto = findViewById(R.id.imageView);
        nameSurname = findViewById(R.id.editTextNameSurname);
        eMail = findViewById(R.id.editTextMail);
        kullaniciAdi = findViewById(R.id.editTextNickname);
        phone = findViewById(R.id.editTextPhone);
        companyName = findViewById(R.id.editTextCompany);
        passwordEditText = findViewById(R.id.editTextPassword);

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
        String imageUrl = util.BASE_URL + util.getPhotoURLPrefix + receivedUser.getUserName() + ".jpg";

        Glide.with(this).load(imageUrl).override(120, 120).into(profilePhoto);
        nameSurname.setText(receivedUser.getNameSurname());
        eMail.setText(receivedUser.geteMail());
        kullaniciAdi.setText(receivedUser.getUserName());
        phone.setText(receivedUser.getPhoneNumber());
        companyName.setText(receivedUser.getCompanyName());
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
