package com.javalon.swifttopup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import io.paperdb.Paper;

public class AirtimeFragment extends Fragment {

    private CardView[] mobileNetworks;
    private String[] priceTag;
    private static String[] networks = {"MTN", "GLO", "9mobile", "AIRTEL"};
    PurchaseDialog purchaseDialog = new PurchaseDialog();

    public AirtimeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View airtimeView = inflater.inflate(R.layout.fragment_airtime, container, false);

        // initialize card views
        mobileNetworks = new CardView[4];
        mobileNetworks[0] = airtimeView.findViewById(R.id.mtnNetwork);
        mobileNetworks[1] = airtimeView.findViewById(R.id.gloNetwork);
        mobileNetworks[2] = airtimeView.findViewById(R.id.nineMobileNetwork);
        mobileNetworks[3] = airtimeView.findViewById(R.id.airtelNetwork);

        priceTag = getResources().getStringArray(R.array.networks);

        // set click listeners on the card views
        for (int i = 0; i < mobileNetworks.length; i++) {
            final int finalI = i;
            mobileNetworks[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    FragmentTransaction transaction = getFragmentManager().beginTransaction();
                    purchaseDialog.setPriceTag(priceTag, String.valueOf("0" + (finalI + 1)));
                    purchaseDialog.setTitle("PURCHASE AIRTIME (" + networks[finalI] + ")");
//                    purchaseDialog.setTag("1");

                    Bundle args = new Bundle();
                    args.putString("tag", "1");
                    purchaseDialog.setArguments(args);

                    purchaseDialog.show(transaction, "transaction");
                }
            });

        }

        return airtimeView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if ((purchaseDialog != null) && (purchaseDialog.isVisible())) {
            purchaseDialog.dismiss();
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