package me.t3sl4.ondergrup;

import android.app.Application;

import com.yariksoffice.lingver.Lingver;

public class DefaultApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Lingver.init(this, "tr");
    }
}