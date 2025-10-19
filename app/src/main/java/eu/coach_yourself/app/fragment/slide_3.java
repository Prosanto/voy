package eu.coach_yourself.app.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.github.appintro.SlidePolicy;

import eu.coach_yourself.app.MyIntro;
import eu.coach_yourself.app.R;

public class slide_3 extends Fragment implements SlidePolicy {
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slide_3, container, false);

        textView = view.findViewById(R.id.t3_slide3);
        textView.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(3000).withEndAction(new Runnable() {
            @Override
            public void run() {
                textView.animate().scaleX(1).scaleY(1).setDuration(3000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        textView.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(3000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                textView.animate().scaleX(1).scaleY(1).setDuration(3000).withEndAction(new Runnable() {
                                    @Override
                                    public void run() {

                                    }
                                });
                            }
                        });
                    }
                });
            }
        });
        return view;

    }

    @Override
    public boolean isPolicyRespected() {

        checkView();
        return true;
    }

    boolean flagScreen = true;

    public void checkView() {
        if (flagScreen) {
            flagScreen = false;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    flagScreen = true;
                    MyIntro myIntro = (MyIntro) getActivity();
                    if (myIntro.currnetPage() == 2) {
                        myIntro.launchMain();
                    }
                }
            }, 500);
        }
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        Log.e("isPolicyRespected", "onUserIllegallyRequestedNextPage");
    }

}
