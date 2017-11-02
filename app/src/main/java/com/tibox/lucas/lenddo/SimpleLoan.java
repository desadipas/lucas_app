package com.tibox.lucas.lenddo;

import android.app.Application;

import com.lenddo.sdk.core.LenddoCoreInfo;

public class SimpleLoan extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LenddoCoreInfo.initCoreInfo(getApplicationContext());
    }
}
