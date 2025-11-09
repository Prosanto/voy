package eu.coach_yourself.app.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PersistentUser {

    //common data
    private static final String PREFS_FILE_NAME = "PersistentUsercoach_yourself";
    private static final String USERDetails = "user_details";
    private static final String USERID = "userid";
    private static final String USERTOKEN = "user_token";
    private static final String USERPASSWORD = "userpassword";
    private static final String PROFILEIMAGEPATH = "image";
    private static final String PUSHTOKEN = "PushToken";
    private static final String keymeditation_Monthlysubscription = "Monthlysubscription";
    private static final String keymeditation_Yearlysubscription = "Yearlysubscription";

    public static void setvolume(Context c, int flag) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().putInt("volume", flag).commit();
    }

    public static int getvolume(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getInt("volume", 50);
    }


//
//    public static void setMonthlysubscription(Context c, boolean flag) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        prefs.edit().putBoolean(keymeditation_Monthlysubscription, flag).commit();
//    }
//
//    public static boolean iSMonthlysubscription(Context c) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        return prefs.getBoolean(keymeditation_Monthlysubscription, false);
//    }
//
//
//    public static void setYearlysubscription(Context c, boolean flag) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        prefs.edit().putBoolean(keymeditation_Yearlysubscription, flag).commit();
//    }
//
//    public static boolean iSYearlysubscription(Context c) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        return prefs.getBoolean(keymeditation_Yearlysubscription, false);
//    }



    public static String getPushToken(final Context ctx) {
        return ctx.getSharedPreferences(PersistentUser.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(PersistentUser.PUSHTOKEN, "");
    }

    public static void setPushToken(final Context ctx, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PersistentUser.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PersistentUser.PUSHTOKEN, data);
        editor.commit();
    }

    public static String getImagePath(final Context ctx) {
        return ctx.getSharedPreferences(PersistentUser.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(PersistentUser.PROFILEIMAGEPATH, "");
    }

    public static void setImagePath(final Context ctx, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PersistentUser.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PersistentUser.PROFILEIMAGEPATH, data);
        editor.commit();
    }

    public static String getUserToken(final Context ctx) {
        return ctx.getSharedPreferences(PersistentUser.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(PersistentUser.USERTOKEN, "");
    }

    public static void setUserToken(final Context ctx, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PersistentUser.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PersistentUser.USERTOKEN, data);
        editor.commit();
    }

    public static String getUserDetails(final Context ctx) {
        return ctx.getSharedPreferences(PersistentUser.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(PersistentUser.USERDetails, "");
    }

    public static void setUserDetails(final Context ctx, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PersistentUser.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PersistentUser.USERDetails, data);
        editor.commit();
    }

    public static String getUserpassword(final Context ctx) {
        return ctx.getSharedPreferences(PersistentUser.PREFS_FILE_NAME,
                Context.MODE_PRIVATE).getString(PersistentUser.USERPASSWORD, "");
    }

    public static void setUserpassword(final Context ctx, final String data) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PersistentUser.PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(PersistentUser.USERPASSWORD, data);
        editor.commit();
    }


    public static String getUserID(final Context ctx) {
        return ctx.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE).getString(USERID, "");
    }

    public static void setUserID(final Context ctx, final String id) {
        final SharedPreferences prefs = ctx.getSharedPreferences(
                PREFS_FILE_NAME, Context.MODE_PRIVATE);
        final Editor editor = prefs.edit();
        editor.putString(USERID, id);
        editor.commit();
    }

    public static void logOut(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().putBoolean("LOGIN", false).commit();

    }

    public static void setLogin(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().putBoolean("LOGIN", true).commit();
    }

    public static boolean isLogged(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("LOGIN", false);
    }
//
//    public static boolean isDashBoardToolsTips(Context c) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        return prefs.getBoolean("DashBoardToolsTips", true);
//    }
//    public static void setDashBoardToolsTips(Context c,boolean flag) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        prefs.edit().putBoolean("DashBoardToolsTips", flag).commit();
//    }
//    public static boolean isPlayerpageToolsTips(Context c) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        return prefs.getBoolean("Playerpage", true);
//    }
//    public static void setPlayerpageToolsTips(Context c,boolean flag) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        prefs.edit().putBoolean("Playerpage", flag).commit();
//    }
//    public static boolean isHomepagesToolsTips(Context c) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        return prefs.getBoolean("Homepage", true);
//    }
//    public static void setHomeToolsTips(Context c,boolean flag) {
//        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
//                Context.MODE_PRIVATE);
//        prefs.edit().putBoolean("Homepage", flag).commit();
//    }

    public static void setLanguage(Context c, boolean flag) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        prefs.edit().putBoolean("Language", flag).commit();
    }

    public static boolean isLanguage(Context c) {
        final SharedPreferences prefs = c.getSharedPreferences(PREFS_FILE_NAME,
                Context.MODE_PRIVATE);
        return prefs.getBoolean("Language", false);
    }
    public static void resetAllData(Context c) {
        setUserID(c, "");
        logOut(c);

    }

}
