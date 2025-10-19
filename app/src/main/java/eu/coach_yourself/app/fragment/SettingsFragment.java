package eu.coach_yourself.app.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import androidx.fragment.app.Fragment;

import co.ceryle.segmentedbutton.SegmentedButtonGroup;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;
import eu.coach_yourself.app.utils.PrefManager;

public class SettingsFragment extends Fragment {
    Button btn_website, btn_term, btn_policy, btn_subscribe;
    Switch switch_tips, swtch_lang, switch_rating;
    private boolean flagClickOption = false;

    private LinearLayout layoutbtn_subscribe;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (PersistentUser.isLanguage(getActivity())) {
            LocaleHelper.setLocale(getContext(), "en");
        } else {
            LocaleHelper.setLocale(getActivity(), "de");
        }
        View view = inflater.inflate(R.layout.setting_fragment, container, false);
        btn_policy = (Button) view.findViewById(R.id.btn_policy);
        btn_term = (Button) view.findViewById(R.id.btn_term);
        btn_website = (Button) view.findViewById(R.id.btn_website);
        btn_subscribe = (Button) view.findViewById(R.id.btn_subscribe);
        switch_rating = (Switch) view.findViewById(R.id.seitch_rating);
        switch_tips = (Switch) view.findViewById(R.id.switch_tips);
        layoutbtn_subscribe = (LinearLayout) view.findViewById(R.id.layoutbtn_subscribe);

        PrefManager prefManager = new PrefManager(getContext());
        btn_website.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://voy-app.com/"));
                startActivity(i);
            }
        });
        btn_term.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlServer="https://voy-app.com/agb/";
                if (PersistentUser.isLanguage(getActivity())) {
                    urlServer="https://voy-app.com/agb/";
                } else {
                    urlServer="https://voy-app.com/agb/";
                }

                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlServer));
                startActivity(i);

            }
        });
        btn_policy.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlServer="https://voy-app.com/datenschutzerklarung/";
                if (PersistentUser.isLanguage(getActivity())) {
                    urlServer="https://voy-app.com/datenschutzerklarung/";
                } else {
                    urlServer="https://voy-app.com/datenschutzerklarung/";
                }
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse(urlServer));
                startActivity(i);

            }
        });
//        btn_subscribe.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Fragment fragment = new purchase();
//                FragmentManager fragmentManager = getChildFragmentManager();
////                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().add(R.id.fragment_setting, fragment)
//                        .addToBackStack("my_fragment_purchase")
//                        .commit();
//
//            }
//        });
//        if (PersistentUser.isLanguage(getActivity())) {
//            layoutbtn_subscribe.setVisibility(View.GONE);
//        } else {
//            layoutbtn_subscribe.setVisibility(View.VISIBLE);
//        }
//
        layoutbtn_subscribe.setVisibility(View.GONE);
//

        flagClickOption = false;

        if (prefManager.IsShowTips()) {
            switch_tips.setChecked(true);
        } else {
            switch_tips.setChecked(false);
        }
        if (prefManager.IsShowRating()) {
            switch_rating.setChecked(true);
        } else {
            switch_rating.setChecked(false);
        }

        switch_tips.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefManager.setShowTips(true);
                    PersistentUser.setPlayerpageToolsTips(getActivity(), true);
                } else {
                    prefManager.setShowTips(false);
                    PersistentUser.setPlayerpageToolsTips(getActivity(), false);

                }
                if (flagClickOption) {

                }
            }
        });
        switch_rating.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    prefManager.setShowRating(true);
                } else {
                    prefManager.setShowRating(false);

                }
            }
        });
        SegmentedButtonGroup segmentedButtonGroup = (SegmentedButtonGroup) view.findViewById(R.id.dynamic_drawable_group);

        if (PersistentUser.isLanguage(getActivity())) {
            segmentedButtonGroup.setPosition(1);
        } else {
            segmentedButtonGroup.setPosition(0);

        }
        segmentedButtonGroup.setOnPositionChangedListener(new SegmentedButtonGroup.OnPositionChangedListener() {
            @Override
            public void onPositionChanged(int position) {
                if (position == 0) {
                    PersistentUser.setLanguage(getActivity(), false);
                    LocaleHelper.setLocale(getActivity(), "de");

                } else {
                    PersistentUser.setLanguage(getActivity(), true);
                    LocaleHelper.setLocale(getActivity(), "en");
                }
                Intent intent = getActivity().getIntent();
                getActivity().overridePendingTransition(0, 0);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("language", true);
                getActivity().finish();
                startActivity(intent);


            }
        });

        return view;
    }

}