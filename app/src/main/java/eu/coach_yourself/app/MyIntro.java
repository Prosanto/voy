package eu.coach_yourself.app;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.github.appintro.AppIntro;
import com.github.appintro.AppIntroPageTransformerType;
import com.nabinbhandari.android.permissions.PermissionHandler;
import com.nabinbhandari.android.permissions.Permissions;

import java.util.List;

import eu.coach_yourself.app.fragment.slide_1;
import eu.coach_yourself.app.fragment.slide_2;
import eu.coach_yourself.app.fragment.slide_3;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;
import eu.coach_yourself.app.utils.PrefManager;

public class MyIntro extends AppIntro {
    PrefManager prefManager;
    private Context mContext;
    private int currentPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (PersistentUser.isLanguage(mContext)) {
            PersistentUser.setLanguage(mContext, true);
            LocaleHelper.setLocale(mContext, "en");
        } else {
            PersistentUser.setLanguage(mContext, false);
            LocaleHelper.setLocale(mContext, "de");
        }

        PersistentUser.setDashBoardToolsTips(mContext, true);
        addSlide(new slide_1());
        addSlide(new slide_2());
        addSlide(new slide_3());
        setTransformer(AppIntroPageTransformerType.Fade.INSTANCE);
        setSkipButtonEnabled(false);
        setButtonsEnabled(false);
        setSwipeLock(true);

        if (Build.VERSION.SDK_INT >= 33) {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE};
            Permissions.check(MyIntro.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                @Override
                public void onGranted() {
                    prefManager = new PrefManager(MyIntro.this);
                    if (!prefManager.IsShowTips()) {
                        launchMain();
                        finish();
                    }
                }
            });

        } else {
            String[] permissions = {Manifest.permission.READ_PHONE_STATE};
            Permissions.check(MyIntro.this/*context*/, permissions, null/*rationale*/, null/*options*/, new PermissionHandler() {
                @Override
                public void onGranted() {
                    prefManager = new PrefManager(MyIntro.this);
                    if (!prefManager.IsShowTips()) {
                        launchMain();
                        finish();
                    }
                }

            });
        }



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
//            Dexter.withContext(this)
//                    .withPermissions(Manifest.permission.READ_PHONE_STATE
//                    ).withListener(new MultiplePermissionsListener() {
//                        @Override
//                        public void onPermissionsChecked(MultiplePermissionsReport report) {
//                            prefManager = new PrefManager(MyIntro.this);
//                            if (!prefManager.IsShowTips()) {
//                                launchMain();
//                                finish();
//                            }
//                        }
//
//                        @Override
//                        public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {/* ... */}
//                    }).check();
//        } else {
//            prefManager = new PrefManager(MyIntro.this);
//            if (!prefManager.IsShowTips()) {
//                launchMain();
//                finish();
//            }

        }


    public void launchMain() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(MyIntro.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public int currnetPage() {
        return currentPage;
    }

    @Override
    protected void onPageSelected(int position) {
        super.onPageSelected(position);
        this.currentPage = position;
        Log.w("as", "position");

    }

    @Override
    protected void onIntroFinished() {
        super.onIntroFinished();
        Log.w("as", "onIntroFinished");

    }

    @Override
    public void setFinishOnTouchOutside(boolean finish) {
        super.setFinishOnTouchOutside(finish);
        Log.w("as", "finish");

    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        super.onSlideChanged(oldFragment, newFragment);

        Log.w("as", "asa");

    }
}