package me.t3sl4.ondergrup.Util;

import static me.t3sl4.ondergrup.Service.UserDataService.getUserFromPreferences;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.yariksoffice.lingver.Lingver;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

import me.t3sl4.ondergrup.R;
import me.t3sl4.ondergrup.Screens.Auth.LoginScreen;
import me.t3sl4.ondergrup.Screens.Dashboard.Engineer;
import me.t3sl4.ondergrup.Screens.Dashboard.Technician;
import me.t3sl4.ondergrup.Service.UserDataService;
import me.t3sl4.ondergrup.Util.HTTP.Requests.User.UserService;

public class Util {
    public Context context;

    public static String userManualURL = "https://hidirektor.com.tr/manual/manual.pdf";

    private static final String TARGET_WIFI_SSID = "OnderGrup_IoT";

    public static String profilePhotoPath;
    public Util(Context context) {
        this.context = context;
        this.profilePhotoPath = context.getFilesDir() + "/profilePhoto/";
    }

    public static boolean isEmpty(TextInputEditText etText) {
        return Objects.requireNonNull(etText.getText()).toString().trim().isEmpty();
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                return networkCapabilities != null &&
                        (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET));
            }
        }
        return false;
    }

    public static boolean isConnectedToTargetWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            Network network = connectivityManager.getActiveNetwork();
            if (network != null) {
                NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(network);
                if (networkCapabilities != null && networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                    if (wifiManager != null) {
                        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                        if (wifiInfo != null) {
                            String ssid = wifiInfo.getSSID().replace("\"", ""); // Remove quotes from SSID
                            return ssid.equals(TARGET_WIFI_SSID);
                        }
                    }
                }
            }
        }
        return false;
    }

    public static void showErrorPopup(Dialog diyalog, String hataMesaji) {
        diyalog.setContentView(R.layout.activity_popup_warning);
        Button close = diyalog.findViewById(R.id.kapatButton);
        TextView hataMesajiTextView = diyalog.findViewById(R.id.uyariMesaji);

        hataMesajiTextView.setText(hataMesaji);
        close.setOnClickListener(v -> diyalog.dismiss());

        Objects.requireNonNull(diyalog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        diyalog.show();
    }

    public static void showSuccessPopup(Dialog diyalog, String hataMesaji) {
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

    public static String convertUnixTimestampToDateString(long unixTimestamp) {
        Date date = new Date(unixTimestamp * 1000L); // Convert seconds to milliseconds
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(date);
    }

    public static void setSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "tr";
        } else if(userLanguage.equals("false")) {
            nextLang = "en";
        } else {
            nextLang = "tr";
        }

        updateLocale(context, nextLang);
    }

    public static void changeSystemLanguage(Context context) {
        String userLanguage = UserDataService.getSelectedLanguage(context);
        String nextLang;

        if (userLanguage.equals("true")) {
            nextLang = "en";
            UserDataService.setSelectedLanguage(context, "false");
        } else {
            nextLang = "tr";
            UserDataService.setSelectedLanguage(context, "true");
        }

        UserService.updatePreferences(context, UserDataService.getUserID(context), UserDataService.getSelectedLanguage(context), UserDataService.getSelectedNightMode(context));

        updateLocale(context, nextLang);
    }

    public static void updateLocale(Context context, String nextLang) {
        Lingver.getInstance().setLocale(context, nextLang);
    }

    public static void redirectBasedRole(Activity activity, boolean splashStatus) {
        Intent intent = null;

        String userRole = UserDataService.getUserRole(activity.getApplicationContext());

        switch (userRole) {
            case "NORMAL":
                intent = new Intent(activity, me.t3sl4.ondergrup.Screens.Dashboard.User.class);
                intent.putExtra("user", getUserFromPreferences(activity.getApplicationContext()));
                break;
            case "TECHNICIAN":
                intent = new Intent(activity, Technician.class);
                intent.putExtra("user", getUserFromPreferences(activity.getApplicationContext()));
                break;
            case "ENGINEER":
                intent = new Intent(activity, Engineer
                        .class);
                intent.putExtra("user", getUserFromPreferences(activity.getApplicationContext()));
                break;
            case "SYSOP":
                Util.showErrorPopup(new Dialog(activity.getApplicationContext()), activity.getResources().getString(R.string.unsupported_sysop));
                break;
            default:
                if(splashStatus) {
                    intent = new Intent(activity, LoginScreen.class);
                    activity.startActivity(intent);
                    activity.finish();
                } else {
                    Util.showErrorPopup(new Dialog(activity.getApplicationContext()), activity.getResources().getString(R.string.unsupported_role));
                }
                break;
        }

        if (intent != null) {
            activity.startActivity(intent);
            activity.finish();
        }
    }
}