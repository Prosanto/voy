package eu.coach_yourself.app.ui.home;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.reflect.TypeToken;
import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;

import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.adapter.CardArrayAdapter;
import eu.coach_yourself.app.fragment.playerFragment;
import eu.coach_yourself.app.model.ModelFile;
import eu.coach_yourself.app.myapplication.Myapplication;
import eu.coach_yourself.app.reservoir.Reservoir;
import eu.coach_yourself.app.utils.AllUrls;
import eu.coach_yourself.app.utils.Helpers;
import eu.coach_yourself.app.utils.PersistentUser;
import eu.coach_yourself.app.utils.PrefManager;
import eu.coach_yourself.app.wenchao.cardstack.CardStack;

public class HomeFragment extends Fragment {

    public String Id;
    private HomeViewModel homeViewModel;
    private TextView txthomeTitle, txt_gopurchase;
    private Button btn_purchase;
    private String TAG = "HomeViewModel";
    private CardStack mCardStack;
    private CardArrayAdapter mCardArrayAdapter;
    private PrefManager prefManager;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @SuppressLint("ResourceType")
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        prefManager = new PrefManager(getContext());
        txthomeTitle = root.findViewById(R.id.text_home);
        txthomeTitle.setVisibility(View.VISIBLE);

        txthomeTitle.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(3000).withEndAction(new Runnable() {
            @Override
            public void run() {
                txthomeTitle.animate().scaleX(1).scaleY(1).setDuration(3000).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        txthomeTitle.animate().scaleX((float) 0.8).scaleY((float) 0.8).setDuration(3000).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                txthomeTitle.animate().scaleX(1).scaleY(1).setDuration(3000).withEndAction(new Runnable() {
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


        btn_purchase = (Button) root.findViewById(R.id.btn_subscribe_home);
        btn_purchase.setVisibility(View.GONE);

//        btn_purchase.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Fragment fragment = new purchase();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                fragmentManager.beginTransaction().add(R.id.fragment_home, fragment)
//                        .addToBackStack("my_fragment_purchase")
//                        .commit();
//            }
//        });

        mCardStack = (CardStack) root.findViewById(R.id.containerCard);
        mCardStack.setCanSwipe(true);
        mCardStack.setEnableRotation(false);
        mCardStack.setListener(new CardStack.CardEventListener() {
            @Override
            public boolean swipeEnd(int section, float distance) {

                return (distance > 300) ? true : false;
                //return false;
            }

            @Override
            public boolean swipeStart(int section, float distance) {
                return false;
            }

            @Override
            public boolean swipeContinue(int section, float distanceX, float distanceY) {
                return false;
            }

            @Override
            public void discarded(int mIndex, int direction) {
                Log.e("discarded", "are" + mIndex);
                if (mIndex == mCardArrayAdapter.getModelFile().size()) {
                    mCardStack.reset(true);
                }
            }

            @Override
            public void topCardTapped() {
                MainActivity mainActivity = (MainActivity) getActivity();
                if (!mainActivity.homeToolTips) {

                    int cardIndex = mCardStack.getCurrIndex();
                    int conentInxe = mCardArrayAdapter.getModelFile().size();
                    if (cardIndex >= conentInxe) {
                        mCardStack.reset(true);

                    }
                    cardIndex = mCardStack.getCurrIndex();
                    ModelFile mModelFile = mCardArrayAdapter.getModelFile(mCardStack.getCurrIndex());
                    if (mModelFile.getSubscription().equalsIgnoreCase("0")) {
                        Myapplication.mModelFile = mModelFile;
                        Myapplication.openScreenType = 0;
                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        Fragment fragment = new playerFragment();
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_home, fragment).addToBackStack(R.class.getName()).commit();

                    } else {

                        Myapplication.mModelFile = mModelFile;
                        Myapplication.openScreenType = 0;
                        AppCompatActivity activity = (AppCompatActivity) getActivity();
                        Fragment fragment = new playerFragment();
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_home, fragment).addToBackStack(R.class.getName()).commit();


//                        boolean monthSubscription = PersistentUser.iSMonthlysubscription(getActivity());
//                        boolean yearSubscription = PersistentUser.iSYearlysubscription(getActivity());
//                        if (monthSubscription || yearSubscription) {
//                            Myapplication.mModelFile = mModelFile;
//                            Myapplication.openScreenType = 0;
//                            AppCompatActivity activity = (AppCompatActivity) getActivity();
//                            Fragment fragment = new playerFragment();
//                            activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_home, fragment).addToBackStack(R.class.getName()).commit();
//                        } else {
//
//                            AppCompatActivity activity = (AppCompatActivity) getActivity();
//                            Fragment fragment = new purchase();
//                            activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment_home, fragment).addToBackStack(R.class.getName()).commit();
//
//
//                        }
                    }
                }

            }
        });

//        if (PersistentUser.isLanguage(getActivity())) {
//            btn_purchase.setVisibility(View.INVISIBLE);
//        } else {
//            btn_purchase.setVisibility(View.VISIBLE);
//            boolean monthSubscription = PersistentUser.iSMonthlysubscription(getActivity());
//            boolean yearSubscription = PersistentUser.iSYearlysubscription(getActivity());
//            if (monthSubscription || yearSubscription) {
//                btn_purchase.setVisibility(View.INVISIBLE);
//            } else {
//                btn_purchase.setVisibility(View.VISIBLE);
//            }
//        }

        if (Helpers.isNetworkAvailable(getActivity())) {
            getData();
        } else {
            loadDataList();
        }
        return root;
    }

    public void device() {

        Log.e("toastMsg", getDeviceDensity());
        Toast.makeText(getActivity(), getDeviceDensity(), Toast.LENGTH_LONG).show();
    }

    private String getDeviceDensity() {
        int density = getActivity().getResources().getDisplayMetrics().densityDpi;
        switch (density) {
            case DisplayMetrics.DENSITY_MEDIUM:
                return "MDPI";
            case DisplayMetrics.DENSITY_HIGH:
                return "HDPI";
            case DisplayMetrics.DENSITY_LOW:
                return "LDPI";
            case DisplayMetrics.DENSITY_XHIGH:
                return "XHDPI";
            case DisplayMetrics.DENSITY_TV:
                return "TV";
            case DisplayMetrics.DENSITY_XXHIGH:
                return "XXHDPI";
            case DisplayMetrics.DENSITY_XXXHIGH:
                return "XXXHDPI";
            default:
                return "Unknown";
        }
    }

    private void getData() {

        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        String url = AllUrls.getCategoryURL(PersistentUser.isLanguage(getActivity()));
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.dismiss();

                try {
                    if (PersistentUser.isLanguage(getActivity())) {
                        Reservoir.put("category_english", response.toString());
                    } else {
                        Reservoir.put("category_german", response.toString());
                    }
                    loadDataList();
                } catch (Exception ex) {
                    Log.e("Exception",ex.getMessage());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley", error.toString());
                progressDialog.dismiss();
                getData();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonArrayRequest);
    }

    public void loadDataList() {

        try {
            String responseServer = "";
            String saveData = "";
            if (PersistentUser.isLanguage(getActivity())) {
                saveData = "category_english";
            } else {
                saveData = "category_german";
            }
            boolean objectExists = Reservoir.contains(saveData);
            if (objectExists) {
                Type resultType = new TypeToken<String>() {
                }.getType();
                responseServer = Reservoir.get(saveData, resultType);
            } else {
                Log.e("no save data","No save data");
                return;
            }
            Log.e("responseServer",responseServer);
            JSONArray jsonArray = new JSONArray(responseServer);
            ArrayList<ModelFile> arrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                ModelFile modelFile = new ModelFile();
                modelFile.setId(jsonObject.getString(("intId")));
                modelFile.setCategoryname(jsonObject.getString("categoryname"));
                modelFile.setCategorytitle(jsonObject.getString("categorytitle"));
                modelFile.setDescription(jsonObject.getString("description"));
                modelFile.setText_color(jsonObject.getString("text_color"));
                modelFile.setDesign_color(jsonObject.getString("text_color"));
                modelFile.setDec_img(jsonObject.getString("category_infoiconimage"));
                modelFile.setCard_image(jsonObject.getString(("card_image")));
                modelFile.setLogo_image(jsonObject.getString(("category_iconimage")));
                modelFile.setText_color(jsonObject.getString("text_color"));
                modelFile.setSubscription(jsonObject.getString("subscription"));
                arrayList.add(modelFile);

            }

            MainActivity mainActivity = (MainActivity) getActivity();
            mCardArrayAdapter = new CardArrayAdapter(getActivity(), R.id.txt_categorytitle, R.layout.home_cards, arrayList, mainActivity.isDevieSmall());
            mCardStack.setAdapter(mCardArrayAdapter);
            mCardStack.reset(true);

            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    boolean IsShowTipsHome = prefManager.IsAlreadyHomepageTips();
                    if (prefManager.IsShowTips() && !IsShowTipsHome) {
                        mainActivity.Tooltip(mCardStack, 1);
                        prefManager.setAlreadyHomepageTips(true);
                    }
                }
            }, 500);

        } catch (Exception ex) {

        }

    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }


}