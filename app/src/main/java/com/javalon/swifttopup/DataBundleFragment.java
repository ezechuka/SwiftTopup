package com.javalon.swifttopup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import io.paperdb.Paper;

public class DataBundleFragment extends Fragment {

    private CardView[] mobileNetworks;
    private String[][] priceTags;
    private static String[] networks = {"MTN", "GLO", "9mobile", "AIRTEL"};
    PurchaseDialog purchaseDialog = new PurchaseDialog();

    public DataBundleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dataBundleView = inflater.inflate(R.layout.fragment_data_bundle, container, false);

        // initialize card views
        mobileNetworks = new CardView[4];
        mobileNetworks[0] = dataBundleView.findViewById(R.id.mtnNetwork);
        mobileNetworks[1] = dataBundleView.findViewById(R.id.gloNetwork);
        mobileNetworks[2] = dataBundleView.findViewById(R.id.nineMobileNetwork);
        mobileNetworks[3] = dataBundleView.findViewById(R.id.airtelNetwork);

        // initialize price tags array
        priceTags = new String[4][];
        priceTags[0] = getResources().getStringArray(R.array.mtnDataPlans);
        priceTags[1] = getResources().getStringArray(R.array.gloDataPlans);
        priceTags[2] = getResources().getStringArray(R.array.nineMobileDataPlans);
        priceTags[3] = getResources().getStringArray(R.array.airtelDataPlans);

        // set click listeners on the card views
        for (int i = 0; i < mobileNetworks.length; i++) {
            final int finalI = i;
            mobileNetworks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    purchaseDialog.setPriceTag(priceTags[finalI], String.valueOf("0" + (finalI + 1)));
                    purchaseDialog.setTitle("PURCHASE DATA BUNDLE (" + networks[finalI] + ")");
//                    purchaseDialog.setTag("2");

                    Bundle args = new Bundle();
                    args.putString("tag", "2");
                    purchaseDialog.setArguments(args);

                    purchaseDialog.show(getFragmentManager(), "");
                }
            });
        }

        return dataBundleView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((purchaseDialog != null) && (purchaseDialog.isVisible())) {
            purchaseDialog.dismissAllowingStateLoss();
            purchaseDialog = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Paper.init(getContext());
        Paper.book().delete(Constants.STATUS);
        Paper.book().delete(Constants.CARD_NUMBER);
        Paper.book().delete(Constants.EXPIRY_DATE);
        Paper.book().delete(Constants.CVV);
    }
}