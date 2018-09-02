package com.sms.code.utils;

import android.content.Context;
import android.content.pm.PackageManager;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * File description
 *
 * @author gao
 * @date 2018/9/2
 */

public class AppUtil {

    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            return -1;
        }
    }

    public static String getJsonFromAssets(Context context, String name) {
        try {
            return readString(context.getAssets().open(name));
        } catch (Throwable e) {
            return null;
        }
    }

    private static String readString(InputStream is) throws IOException {

        InputStreamReader reader = new InputStreamReader(is, "UTF-8");
        StringBuilder out = new StringBuilder();

        try {
            int len = 0;
            char buffer[] = new char[8 * 1024];

            while ((len = reader.read(buffer, 0, buffer.length)) > 0) {
                out.append(buffer, 0, len);
            }
        } catch (EOFException e) {
            e.printStackTrace();
        } finally {
            reader.close();
        }

        return out.toString();
    }

    public static boolean isSameDay(long time) {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        String sp_time = sf.format(time);
        String current_time = sf.format(timeMillis);

        return sp_time.equals(current_time);
    }
}
