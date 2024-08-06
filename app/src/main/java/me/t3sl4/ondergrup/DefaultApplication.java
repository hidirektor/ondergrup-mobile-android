package me.t3sl4.ondergrup;

import android.app.Application;

import me.t3sl4.ondergrup.Util.Util;

public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Util.setSystemLanguage(this);
    }
}