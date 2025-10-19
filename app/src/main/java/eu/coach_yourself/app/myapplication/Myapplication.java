package eu.coach_yourself.app.myapplication;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import androidx.multidex.MultiDex;
import com.activeandroid.ActiveAndroid;
import java.io.IOException;

import eu.coach_yourself.app.model.ModelFile;
import eu.coach_yourself.app.reservoir.Reservoir;


public class Myapplication extends Application {
    public static final String SUBSCRIPTION_Monthly_subscription = "coach_yourself_training";
    public static final String SUBSCRIPTION_Yearly_subscription = "voy_jahr";
    public static final String LICENSE_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhwOt28ydoSKUYX1cZMgy6xahGfRMFHqGBQMolB/T7iLifqWNJpOHGK2aWweaqfAdTPP9hIxo2cARF8dGRDkxY02RxI6a56asruJ0l6LZN6hjZgu8nGNwj1o9VeZM14W1jE9bhyhMPDq9zwOEXXF5YQY879Mxx4xRjcSePeKOaXM+ax+tEfp4zcSug7mYzP6ykyJewzjXtzI+8ZmCUtZeQUSupdgZiCvjWJbq8gg1d8tIik5hfci/JywfC6M14H63xk3hGXn0/+/ykiw/D8ss9GzD29bJitnf4uE8JrHpKf5fXjysWYRTj31ap+bwptquEDj1Hx7x5uDV9+CP+gLH6QIDAQAB"; // PUT YOUR MERCHANT KEY HERE;
    public static final String MERCHANT_ID = "16230687707354144378";
    public static ModelFile mModelFile =null;
    public static int openScreenType =0;
    public static String ratingsID ="";


    @Override
    public void onCreate() {
        super.onCreate();
        ActiveAndroid.initialize(this);
        try {
            Reservoir.init(this, 1024 * 1024 * 10); //in bytes
           // Reservoir.init(this, 2048); //in bytes
        } catch (IOException e) {
            Log.e("IOException","are"+e.getMessage());
            //failure
        }

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        MultiDex.install(this);
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }



}
