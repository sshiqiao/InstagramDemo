package com.start.utils;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;


public class IGApplication extends Application {
    private static IGApplication instance;
    public static IGApplication getGlobalContext() {
        return instance;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        Fresco.initialize(this);
    }
}
