package eu.coach_yourself.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PowerManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import eu.coach_yourself.app.database.AlarmData;
import eu.coach_yourself.app.database.DatabaseQueryHelper;
import eu.coach_yourself.app.service.AlertReceiver;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;
import eu.coach_yourself.app.utils.PrefManager;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltip;
import io.github.douglasjunior.androidSimpleTooltip.SimpleTooltipUtils;

public class MainActivity extends AppCompatActivity {
    PrefManager prefManager;
    private Context mContext;
    BottomNavigationView navView;
    public boolean homeToolTips = false;
    private static final String LOG_TAG = "iabv3";
    private BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        PowerManager powerManager = (PowerManager) mContext.getSystemService(mContext.POWER_SERVICE);
        PowerManager.WakeLock  wakeLock = powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK |
                PowerManager.ACQUIRE_CAUSES_WAKEUP |
                PowerManager.ON_AFTER_RELEASE, "appname::WakeLock");
        wakeLock.acquire();
        wakeLock.release();

        navView = findViewById(R.id.nav_view);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_fav, R.id.navigation_graph, R.id.navigation_setting, R.id.navigation_motivator)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupWithNavController(navView, navController);
        prefManager = new PrefManager(getApplicationContext());
        prefManager.setFirstTimeLaunch(false);
        boolean flagScreen = getIntent().getBooleanExtra("language", false);
        if (flagScreen) {
            navView.getMenu().findItem(R.id.navigation_setting).setChecked(true);
            navView.setSelectedItemId(R.id.navigation_setting);
        }
//        bp = new BillingProcessor(this, Myapplication.LICENSE_KEY, Myapplication.MERCHANT_ID, new BillingProcessor.IBillingHandler() {
//            @Override
//            public void onProductPurchased(String productId, PurchaseInfo purchaseInfo) {
//                // showToast("onProductPurchased: " + productId);
//            }
//
//            @Override
//            public void onBillingError(int errorCode, Throwable error) {
//                // showToast("onBillingError: " + Integer.toString(errorCode));
//            }
//
//            @Override
//            public void onBillingInitialized() {
//                checkStatus();
//            }
//
//            @Override
//            public void onPurchaseHistoryRestored() {
//            }
//        });
        if (PersistentUser.isLanguage(mContext)) {
            PersistentUser.setLanguage(mContext, true);
            LocaleHelper.setLocale(mContext, "en");
        } else {
            PersistentUser.setLanguage(mContext, false);
            LocaleHelper.setLocale(mContext, "de");
        }

    }

    public void Tooltip(View view, int postion) {
        homeToolTips = true;
        String tool_tips_card = getResources().getString(R.string.tool_tips_card);
        String tool_tips_motivator = getResources().getString(R.string.tool_tips_motivator);
        String tool_tips_char = getResources().getString(R.string.tool_tips_char);
        if (postion == 1) {
            new SimpleTooltip.Builder(mContext)
                    .anchorView(view)
                    .text(tool_tips_card)
                    .gravity(Gravity.BOTTOM)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            MenuItem menuItem = navView.getMenu().findItem(R.id.navigation_motivator);
                            int id = menuItem.getItemId();
                            View view = findViewById(id);
                            Tooltip(view, 2);
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        } else if (postion == 2 || postion == 3) {
            String showText = "";
            if (postion == 2)
                showText = tool_tips_motivator;
            else if (postion == 3)
                showText = tool_tips_char;

            new SimpleTooltip.Builder(mContext)
                    .anchorView(view)
                    .text(showText)
                    .gravity(Gravity.TOP)
                    .transparentOverlay(false)
                    .overlayWindowBackgroundColor(Color.BLACK)
                    .backgroundColor(Color.WHITE)
                    .arrowColor(Color.WHITE)
                    .textColor(Color.BLACK)
                    .animationPadding(SimpleTooltipUtils.pxFromDp(50))
                    .contentView(R.layout.tooltip_custom, R.id.tv_text)
                    .onDismissListener(new SimpleTooltip.OnDismissListener() {
                        @Override
                        public void onDismiss(SimpleTooltip tooltip) {
                            if (postion == 2) {
                                MenuItem menuItem = navView.getMenu().findItem(R.id.navigation_graph);
                                int id = menuItem.getItemId();
                                View view = findViewById(id);
                                Tooltip(view, 3);
                            } else if (postion == 3) {
                                prefManager.setShowTips(false);
                                PersistentUser.setDashBoardToolsTips(mContext, false);
                                homeToolTips = false;

                            }
                        }
                    })
                    .onShowListener(new SimpleTooltip.OnShowListener() {
                        @Override
                        public void onShow(SimpleTooltip tooltip) {
                        }
                    })
                    .build()
                    .show();
        }

    }

    public boolean gethomeToolTips() {
        return homeToolTips;
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
        super.onBackPressed();
    }

    public void BackButtonAction() {
        FragmentManager fm = getSupportFragmentManager();
        for (Fragment frag : fm.getFragments()) {
            if (frag.isVisible()) {
                FragmentManager childFm = frag.getChildFragmentManager();
                if (childFm.getBackStackEntryCount() > 0) {
                    childFm.popBackStack();
                    return;
                }
            }
        }
    }

    List<String> owned = new ArrayList<>();
    List<String> subscriptions = new ArrayList<>();
    ProgressDialog pd;

//    private void checkStatus() {
//        new AsyncTask<Void, Void, Boolean>() {
//            @Override
//            protected Boolean doInBackground(Void... voids) {
//                owned.clear();
//                subscriptions.clear();
//                owned = bp.listOwnedProducts();
//                subscriptions = bp.listOwnedSubscriptions();
//
//                if (subscriptions.contains(Myapplication.SUBSCRIPTION_Monthly_subscription))
//                    PersistentUser.setMonthlysubscription(mContext, true);
//                else
//                    PersistentUser.setMonthlysubscription(mContext, false);
//
//                if (subscriptions.contains(Myapplication.SUBSCRIPTION_Yearly_subscription))
//                    PersistentUser.setYearlysubscription(mContext, true);
//                else
//                    PersistentUser.setYearlysubscription(mContext, false);
//
//
//                return true;
//            }
//
//            @Override
//            protected void onPostExecute(Boolean b) {
//                super.onPostExecute(b);
//                if (pd != null)
//                    pd.dismiss();
//
//            }
//        }.execute();
//    }

    public boolean isDevieSmall() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;

        if (height > 1200) {
            return true;
        } else {
            return false;
        }
    }
    public void removeReminder(long id) {
        ArrayList<AlarmData> allAlarmData = DatabaseQueryHelper.getAlarmData("" + id);
        for (AlarmData mAlarmData : allAlarmData) {
            long idalarm = mAlarmData.getId();
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            Intent myIntent = new Intent(mContext, AlertReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, (int) idalarm, myIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarmManager.cancel(pendingIntent);
        }
        DatabaseQueryHelper.DeleteAlarmData("" + id);


    }


}