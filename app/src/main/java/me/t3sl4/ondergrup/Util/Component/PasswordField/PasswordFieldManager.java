package me.t3sl4.ondergrup.Util.Component.PasswordField;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.EditText;

import androidx.annotation.DrawableRes;
import androidx.core.content.res.ResourcesCompat;

import me.t3sl4.ondergrup.R;

public class PasswordFieldManager {
    private static boolean isPasswordVisible = false;

    public static void togglePasswordVisibility(EditText editTextPassword, Context context) {
        isPasswordVisible = !isPasswordVisible;
        int drawableResId = isPasswordVisible ? R.drawable.ikon_passhide : R.drawable.ikon_passshow;
        setPasswordVisibility(isPasswordVisible, editTextPassword);
        updatePasswordToggleIcon(drawableResId, editTextPassword, context);
    }

    private static void setPasswordVisibility(boolean visible, EditText editTextPassword) {
        int inputType = visible ?
                android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD :
                android.text.InputType.TYPE_CLASS_TEXT | android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;
        editTextPassword.setInputType(inputType);
        editTextPassword.setSelection(editTextPassword.getText().length());
    }

    private static void updatePasswordToggleIcon(@DrawableRes int drawableResId, EditText editTextPassword, Context context) {
        Drawable[] drawables = editTextPassword.getCompoundDrawablesRelative();
        drawables[2] = ResourcesCompat.getDrawable(context.getResources(), drawableResId, context.getTheme());
        editTextPassword.setCompoundDrawablesRelativeWithIntrinsicBounds(
                drawables[0], drawables[1], drawables[2], drawables[3]);
    }
}
