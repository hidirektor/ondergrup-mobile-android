package me.t3sl4.countrypicker;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.List;

import me.t3sl4.countrypicker.Model.Country;

public class CountryData {

    public static List<Country> getCountries(Context context) {
        String json = loadJSONFromAsset(context);
        Gson gson = new Gson();
        Type listType = new TypeToken<List<Country>>() {}.getType();
        return gson.fromJson(json, listType);
    }

    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("country_list.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}