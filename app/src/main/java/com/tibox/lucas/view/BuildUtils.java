package com.tibox.lucas.view;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by desa02 on 02/03/2017.
 */

public class BuildUtils {
    private static final File BUILD_PROP_FILE = new File(Environment.getRootDirectory(), "build.prop");
    private static Properties sBuildProperties;
    private static final Object sBuildPropertiesLock = new Object();

    public static Properties getBuildProperties() {
        synchronized (sBuildPropertiesLock) {
            if (sBuildProperties == null) {
                sBuildProperties = new Properties();
                try {
                    sBuildProperties.load(new FileInputStream(BUILD_PROP_FILE));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sBuildProperties;
    }

//    public static boolean isEmotionUi() {
//        return getBuildProperties().containsKey("ro.build.version.emui");
//    }

//    public static String getEmotionUiVersion() {
//        return getBuildProperties().getProperty("ro.build.version.emui");
//    }

    public static boolean isMIUI() {
        return getBuildProperties().containsKey("ro.miui.ui.version.name");
    }
}