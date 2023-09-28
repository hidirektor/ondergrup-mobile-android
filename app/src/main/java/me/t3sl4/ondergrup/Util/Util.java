package me.t3sl4.ondergrup.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.DrawableRes;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Util.User.User;

public class Util {
    public Context context;

    public static User user = new User("1", "1", "1", "1", "1", "1", "1");

    public String BASE_URL = "http://85.95.231.92:3000";

    public String loginURLPrefix = "/api/login";
    public String getPassURLPrefix = "/api/getCipheredPass";
    public String directLoginURLPrefix = "/api/directLogin";
    public String registerURLPrefix = "/api/register";
    public String otpURLPrefix = "/api/sendOTP";
    public String addSubURLPrefix = "/api/createSub";


    public String profileInfoURLPrefix = "/api/users/profileInfo/";
    public String updatePassURLPrefix = "/api/users/updatePass";
    public String updateProfileURLPrefix = "/api/users/updateProfile";
    public String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";
    public String getPhotoURLPrefix = "/api/fileSystem/getPhoto/";


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

    public void showErrorPopup(Dialog diyalog, String hataMesaji) {
        diyalog.setContentView(R.layout.activity_popup_warning);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TextView hataMesajiTextView = diyalog.findViewById(R.id.uyariMesaji);

        hataMesajiTextView.setText(hataMesaji);
        close.setOnClickListener(v -> diyalog.dismiss());

        diyalog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
    }

    public static String dateTimeConvert(String inputDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss");

        try {
            Date date = inputDateFormat.parse(inputDate);
            return outputDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
