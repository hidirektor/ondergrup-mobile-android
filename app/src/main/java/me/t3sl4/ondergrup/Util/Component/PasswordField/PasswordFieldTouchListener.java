package me.t3sl4.ondergrup.Util.Component.PasswordField;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputLayout;

public class PasswordFieldTouchListener {
    private static boolean isPasswordVisible = false;
    @SuppressLint("ClickableViewAccessibility")
    public static void setChangeablePasswordField(EditText editTextPassword, Context context) {
        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        PasswordFieldManager.togglePasswordVisibility(editTextPassword, context);
                        isPasswordVisible = !isPasswordVisible;
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    public static void setChangeablePasswordFieldLayout(TextInputLayout textInputLayoutPassword, Context context) {
        EditText editTextPassword = textInputLayoutPassword.getEditText();

        if (editTextPassword == null) {
            return;
        }

        textInputLayoutPassword.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);

        editTextPassword.setOnTouchListener(new View.OnTouchListener() {
            final int DRAWABLE_RIGHT = 2;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (editTextPassword.getRight() - editTextPassword.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        PasswordFieldManager.togglePasswordVisibility(editTextPassword, context);
                        isPasswordVisible = !isPasswordVisible;
                        return true;
                    }
                }
                return false;
            }
        });
    }

}
