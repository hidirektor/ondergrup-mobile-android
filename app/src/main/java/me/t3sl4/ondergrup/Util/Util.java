package me.t3sl4.ondergrup.Util;

import android.content.Context;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;

public class Util {
    public Context context;

    public String BASE_URL = "http://85.95.231.92:3000";

    public String loginURLPrefix = "/api/login";
    public String getPassURLPrefix = "/api/getCipheredPass";
    public String directLoginURLPrefix = "/api/directLogin";
    public String registerURLPrefix = "/api/register";
    public String otpURLPrefix = "/api/sendOTP";


    public String profileInfoURLPrefix = "/api/users/profileInfo/";
    public String updatePassURLPrefix = "/api/users/updatePass";
    public String updateProfileURLPrefix = "/api/users/updateProfile";
    public String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";


    public String uploadURLPrefix = "/api/fileSystem/upload";
    public String downloadPhotoURLPrefix = "/api/fileSystem/downloadPhoto";

    public static String profilePhotoPath;
    public Util(Context context) {
        this.context = context;
        this.profilePhotoPath = context.getFilesDir() + "/profilePhoto/";
    }

    public boolean isEmpty(TextInputEditText etText) {
        if (etText.getText().toString().trim().length() > 0)
            return false;

        return true;
    }
}
