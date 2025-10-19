package eu.coach_yourself.app.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.PurchaseInfo;

import eu.coach_yourself.app.MainActivity;
import eu.coach_yourself.app.R;
import eu.coach_yourself.app.myapplication.Myapplication;
import eu.coach_yourself.app.utils.LocaleHelper;
import eu.coach_yourself.app.utils.PersistentUser;


public class purchase extends Fragment {
    TextView txt_detail;
    private static final String LOG_TAG = "iabv3";
    private BillingProcessor bp;
    private boolean readyToPurchase = false;
    private Button btn_bottom1;
    private Button btn_bottom2;
    private AppCompatButton btn_purchase_month;
    private AppCompatButton btn_purchase_year;

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_purchase, container, false);
        Toolbar toolbar = view.findViewById(R.id.toolbarId);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getParentFragmentManager().getBackStackEntryCount() > 0) {
                    getParentFragmentManager().popBackStack();
                } else {
                    MainActivity mainActivity = (MainActivity) getActivity();
                    mainActivity.BackButtonAction();
                }

            }
        });
        if (PersistentUser.isLanguage(getActivity())) {
            LocaleHelper.setLocale(getContext(), "en");
        } else {
            LocaleHelper.setLocale(getActivity(), "de");
        }
        btn_bottom1 = (Button) view.findViewById(R.id.btn_bottom1);
        btn_bottom2 = (Button) view.findViewById(R.id.btn_bottom2);
        btn_purchase_month = (AppCompatButton) view.findViewById(R.id.btn_purchase_month);
        btn_purchase_year = (AppCompatButton) view.findViewById(R.id.btn_purchase_year);
        btn_bottom1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://voy-app.com/datenschutzerklarung/"));
                startActivity(i);
            }
        });
        btn_bottom2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setAction(Intent.ACTION_VIEW);
                i.setData(Uri.parse("https://voy-app.com/impressum/"));
                startActivity(i);
            }
        });
        btn_purchase_month.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(getActivity(), Myapplication.SUBSCRIPTION_Monthly_subscription);

            }
        });
        btn_purchase_year.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bp.subscribe(getActivity(), Myapplication.SUBSCRIPTION_Yearly_subscription);

            }
        });


        InappBillingInitial();
        return view;
    }

    public void InappBillingInitial() {
        bp = new BillingProcessor(getActivity(), Myapplication.LICENSE_KEY, Myapplication.MERCHANT_ID, new BillingProcessor.IBillingHandler() {
            @Override
            public void onProductPurchased(String productId, PurchaseInfo details) {
//                if (productId.equalsIgnoreCase(Myapplication.SUBSCRIPTION_Monthly_subscription)) {
//                    PersistentUser.setMonthlysubscription(getActivity(), true);
//                    Intent intent = getActivity().getIntent();
//                    getActivity().overridePendingTransition(0, 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    intent.putExtra("language", false);
//                    getActivity().finish();
//                    startActivity(intent);
//
//                } else if (productId.equalsIgnoreCase(Myapplication.SUBSCRIPTION_Yearly_subscription)) {
//                    PersistentUser.setYearlysubscription(getActivity(), true);
//                    Intent intent = getActivity().getIntent();
//                    getActivity().overridePendingTransition(0, 0);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                    intent.putExtra("language", false);
//                    getActivity().finish();
//                    startActivity(intent);
//                }

            }

            @Override
            public void onBillingError(int errorCode, Throwable error) {
            }

            @Override
            public void onBillingInitialized() {
                readyToPurchase = true;

            }

            @Override
            public void onPurchaseHistoryRestored() {
                for (String sku : bp.listOwnedProducts())
                    Log.d(LOG_TAG, "Owned Managed Product: " + sku);
                for (String sku : bp.listOwnedSubscriptions())
                    Log.d(LOG_TAG, "Owned Subscription: " + sku);
            }
        });
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (!bp.(requestCode, resultCode, data))
//            super.onActivityResult(requestCode, resultCode, data);
//    }


}