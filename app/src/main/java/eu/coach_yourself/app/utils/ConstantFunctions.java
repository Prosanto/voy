package eu.coach_yourself.app.utils;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;


public class ConstantFunctions {
    private static final String TAG = ConstantFunctions.class.getSimpleName();


    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static String getPhoneName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    public static int getScreenHeight() {

        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    private static String capitalize(String str) {
        if (TextUtils.isEmpty(str)) {
            return str;
        }
        char[] arr = str.toCharArray();
        boolean capitalizeNext = true;
        String phrase = "";
        for (char c : arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase += Character.toUpperCase(c);
                capitalizeNext = false;
                continue;
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true;
            }
            phrase += c;
        }
        return phrase;
    }

    public static int getGridItemSize(int NUM_OF_COLUMNS) {
        return (int) (Resources.getSystem().getDisplayMetrics().widthPixels - 50) / NUM_OF_COLUMNS;
    }



    public static void webIntntCall(Context mContext, String web_url) {
        Intent newIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(web_url));
        newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(newIntent);

    }

    public static String trackurlSplit(String path) {
        String pathReturn = "";
        String[] separated = path.split("/");
        if (separated.length > 0) {
            pathReturn = separated[separated.length - 1];
        }
        return pathReturn;
    }

}
