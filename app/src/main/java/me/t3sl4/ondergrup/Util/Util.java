package me.t3sl4.ondergrup.Util;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Model.User.User;
import me.t3sl4.ondergrup.SplashActivity;
import me.t3sl4.ondergrup.Util.Component.SharedPreferencesManager;

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
    public String getSubUsersPrefix = "/api/getSubUsers";
    public String deleteSubUserPrefix = "/api/deleteSubUser";


    public String profileInfoURLPrefix = "/api/users/profileInfo/";
    public String updatePassURLPrefix = "/api/users/updatePass";
    public String updateProfileURLPrefix = "/api/users/updateProfile";
    public String wholeProfileURLPrefix = "/api/users/getWholeProfileInfo";
    public String getPhotoURLPrefix = "/api/fileSystem/getPhoto/";

    public String addMachineURL = "/api/machine/add";
    public String getMachineURL = "/api/machine/getMachines";
    public String getAllMachinesURL = "/api/machine/getAllMachines";
    public String getMachineErrorURL = "/api/machine/getMachineErrors";
    public String getMachineMaintenanceURL = "/api/machine/getMachineMaintenances";
    public String getMachineErrorAllURL = "/api/machine/getMachineErrorsAll";
    public String getMachineMaintenanceAllURL = "/api/machine/getMachineMaintenancesAll";


    public String uploadURLPrefix = "/api/fileSystem/upload";
    public String downloadPhotoURLPrefix = "/api/fileSystem/downloadPhoto";
    public String manualPDFUrlPrefix = "https://hidirektor.com.tr/manual/manual.pdf";

    public static String profilePhotoPath;
    public Util(Context context) {
        this.context = context;
        this.profilePhotoPath = context.getFilesDir() + "/profilePhoto/";
    }

    public boolean isEmpty(TextInputEditText etText) {
        if (Objects.requireNonNull(etText.getText()).toString().trim().length() > 0)
            return false;

        return true;
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showErrorPopup(Dialog diyalog, String hataMesaji) {
        diyalog.setContentView(R.layout.activity_popup_warning);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TextView hataMesajiTextView = diyalog.findViewById(R.id.uyariMesaji);

        hataMesajiTextView.setText(hataMesaji);
        close.setOnClickListener(v -> diyalog.dismiss());

        Objects.requireNonNull(diyalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
    }

    public void showSuccessPopup(Dialog diyalog, String hataMesaji) {
        diyalog.setContentView(R.layout.activity_popup_success);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TextView hataMesajiTextView = diyalog.findViewById(R.id.uyariMesaji);

        hataMesajiTextView.setText(hataMesaji);
        close.setOnClickListener(v -> diyalog.dismiss());

        Objects.requireNonNull(diyalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
    }

    public static String dateTimeConvert(String inputDate) {
        SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        inputDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputDateFormat = new SimpleDateFormat("MM.dd.yyyy hh:mm:ss");

        try {
            Date date = inputDateFormat.parse(inputDate);
            assert date != null;
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

    public static void setLocale(Context context, String newLanguage) {
        Resources activityRes = context.getResources();
        Configuration activityConf = activityRes.getConfiguration();
        Locale newLocale = new Locale(newLanguage);
        activityConf.setLocale(newLocale);
        activityRes.updateConfiguration(activityConf, activityRes.getDisplayMetrics());

        Resources applicationRes = context.getResources();
        Configuration applicationConf = applicationRes.getConfiguration();
        applicationConf.setLocale(newLocale);
        applicationRes.updateConfiguration(applicationConf,
                applicationRes.getDisplayMetrics());
    }

    public static void loadNewTranslations(Context context) {
        String currentLanguage = SharedPreferencesManager.getSharedPref("language", context, "en");

        Util.setLocale(context, currentLanguage);
    }
}
