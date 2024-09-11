package me.t3sl4.countrypicker.Model;

import android.content.Context;

public class Country {
    private String name;
    private String flag;
    private String code;

    public Country(String name, String flag, String code) {
        this.name = name;
        this.flag = flag;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public String getFlag() {
        return flag;
    }

    public String getCode() {
        return code;
    }

    public int getFlagResId(Context context) {
        return context.getResources().getIdentifier(flag, "drawable", context.getPackageName());
    }
}