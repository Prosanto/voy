package eu.coach_yourself.app.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import eu.coach_yourself.app.R;
import eu.coach_yourself.app.adapter.AdapterFavourite;
import eu.coach_yourself.app.callbackinterface.FilterItemCallback;
import eu.coach_yourself.app.database.DatabaseQueryHelper;
import eu.coach_yourself.app.database.FavouriteSongs;
import eu.coach_yourself.app.model.ModelFile;
import eu.coach_yourself.app.myapplication.Myapplication;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;

public class FavFragment extends BaseFragment {
    RecyclerView recycle_fav;
    TextView txt_favdetail;
    AdapterFavourite adapterFavourite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_fav, container, false);
        recycle_fav = (RecyclerView) view.findViewById(R.id.recycle_fav);
        txt_favdetail = (TextView) view.findViewById(R.id.txt_fav);
        if (PersistentUser.isLanguage(getActivity())) {
            LocaleHelper.setLocale(getContext(), "en");
        } else {
            LocaleHelper.setLocale(getActivity(), "de");
        }
        return view;
    }

    public void showData() {
        ArrayList<FavouriteSongs> allArrayList = DatabaseQueryHelper.getFavouriteSongs();
        if (allArrayList.size() > 0) {
            txt_favdetail.setVisibility(View.GONE);
            recycle_fav.setVisibility(View.VISIBLE);
        } else {
            txt_favdetail.setVisibility(View.VISIBLE);
            recycle_fav.setVisibility(View.GONE);
        }
        adapterFavourite = new AdapterFavourite(getContext(), allArrayList);
        recycle_fav.setHasFixedSize(true);
        recycle_fav.setLayoutManager(new LinearLayoutManager(getContext()));
        recycle_fav.setItemAnimator(new DefaultItemAnimator());
        recycle_fav.setAdapter(adapterFavourite);
        adapterFavourite.notifyDataSetChanged();
        adapterFavourite.setOnItemClickListener(lFilterItemCallback);


    }

    public FilterItemCallback lFilterItemCallback = new FilterItemCallback() {
        @Override
        public void ClickFilterItemCallback(int type, int position) {
            //setCategory_iconimage
            FavouriteSongs mClientList = adapterFavourite.getModelAt(position);
            ModelFile modelFile = new ModelFile();
            modelFile.setId(mClientList.getCategoryid());
            modelFile.setCategoryname(mClientList.getCategoryname());
            modelFile.setCategorytitle(mClientList.getCategorytitle());
            modelFile.setDescription(mClientList.getDescription());
            modelFile.setText_color(mClientList.getText_color());
            modelFile.setDesign_color(mClientList.getDesign_color());
            modelFile.setDec_img(mClientList.getCategory_iconimage());
            modelFile.setCard_image(mClientList.getCard_image());
            modelFile.setLogo_image(mClientList.getCategory_iconimage());
            modelFile.setSubscription(mClientList.getSubscription());
            modelFile.setContentId(mClientList.getSongsID());

            Myapplication.mModelFile = modelFile;
            Myapplication.openScreenType = 4;

            Fragment fragment = new playerFragment();
            getChildFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_fav, fragment)
                    .addToBackStack(R.class.getName()).commit();

        }
    };

    @Override
    protected void onVisible() {
        Log.e("onVisible", "onVisible");
        showData();
    }

    @Override
    protected void onInvisible() {
        Log.e("onInvisible", "onInvisible");

    }
}