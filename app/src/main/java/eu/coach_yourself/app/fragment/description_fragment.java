package eu.coach_yourself.app.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.model.ModelFile;
import eu.coach_yourself.app.myapplication.Myapplication;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;

public class description_fragment extends Fragment {
    private TextView txt_dec_title, txt_dec;
    private ImageView img_dec_logo;
    private RelativeLayout fragment_detail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_description, container, false);
        if (PersistentUser.isLanguage(getActivity())) {
            LocaleHelper.setLocale(getContext(), "en");
        } else {
            LocaleHelper.setLocale(getActivity(), "de");
        }
        Toolbar toolbar = v.findViewById(R.id.toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity mainActivity = (MainActivity) getActivity();
                mainActivity.BackButtonAction();

            }
        });
        Bundle bundle = this.getArguments();
        ModelFile mModelFile = Myapplication.mModelFile;
        txt_dec = (TextView) v.findViewById(R.id.txt_dec);
        fragment_detail = (RelativeLayout) v.findViewById(R.id.fragment_detail);
        txt_dec_title = (TextView) v.findViewById(R.id.txt_dec_title);
        img_dec_logo = (ImageView) v.findViewById(R.id.img_dec_logo);
        txt_dec_title.setText(mModelFile.getCategoryname());
        txt_dec_title.setTextColor(Color.parseColor(mModelFile.getText_color()));
        txt_dec.setText(mModelFile.getDescription());
        Picasso.with(getContext()).load(mModelFile.getDec_img()).into(img_dec_logo);
        fragment_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        return v;
    }

}