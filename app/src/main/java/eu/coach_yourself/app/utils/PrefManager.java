package eu.coach_yourself.app.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;
    Context context;
    private static final String FIRST_LAUNCH = "firstLaunch";
    private static final String SHOW_RATTING = "show_ratting";
    private static final String SHOW_TIPS = "show_tips";

    int MODE = 0;
    private static final String PREFERENCE = "Java";

    public PrefManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE, MODE);
        spEditor = sharedPreferences.edit();
    }

    public void setFirstTimeLaunch(boolean isFirstTime) {
        spEditor.putBoolean(FIRST_LAUNCH, isFirstTime);
        spEditor.commit();
    }
    public boolean FirstLaunch() {
        return sharedPreferences.getBoolean(FIRST_LAUNCH, true);
    }
    public void setShowRating(boolean isFirstTime) {
        spEditor.putBoolean(SHOW_RATTING, isFirstTime);
        spEditor.commit();
    }
    public boolean IsShowRating() {
        return sharedPreferences.getBoolean(SHOW_RATTING, true);
    }
    public void setShowTips(boolean isFirstTime) {
        spEditor.putBoolean(SHOW_TIPS, isFirstTime);
        spEditor.commit();
    }

    public boolean IsShowTips() {
        return sharedPreferences.getBoolean(SHOW_TIPS, true);
    }

}
