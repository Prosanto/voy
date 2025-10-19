package eu.coach_yourself.app.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import eu.coach_yourself.app.R;

public class slide_2 extends Fragment {

    TextView textView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.slide_2, container, false);

        textView=view.findViewById(R.id.t3_slide2);
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
                            } });
                    }
                });
            }
        });
        return view;

    }
}
