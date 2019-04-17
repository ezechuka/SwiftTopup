package com.javalon.cklite;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javalon.cklite.airtime.PurchaseAirtimeFragment;
import com.javalon.cklite.dataBundle.PurchaseDataBundleActivity;
import com.javalon.cklite.dataBundle.PurchaseDataBundleFragment;


public class DataBundleFragment extends Fragment {

    private CardView mtnNetwork;
    private CardView gloNetwork;
    private CardView nineMobileNetwork;
    private CardView airtelNetwork;

    public DataBundleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View dataBundleView = inflater.inflate(R.layout.fragment_data_bundle, container, false);
        // initialize card views
        mtnNetwork = dataBundleView.findViewById(R.id.mtnNetwork);
        gloNetwork = dataBundleView.findViewById(R.id.gloNetwork);
        nineMobileNetwork = dataBundleView.findViewById(R.id.nineMobileNetwork);
        airtelNetwork = dataBundleView.findViewById(R.id.airtelNetwork);

        // set click listeners on the card views
        // MTN
        mtnNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseDataBundleActivity.class);
                intent.putExtra(PurchaseDataBundleFragment.MOBILE_NETWORK, "01");
                startActivity(intent);
            }
        });

        // GLO
        gloNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseDataBundleActivity.class);
                intent.putExtra(PurchaseDataBundleFragment.MOBILE_NETWORK, "02");
                startActivity(intent);
            }
        });

        // 9mobile
        nineMobileNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseDataBundleActivity.class);
                intent.putExtra(PurchaseDataBundleFragment.MOBILE_NETWORK, "03");
                startActivity(intent);
            }
        });

        // airtel
        airtelNetwork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PurchaseDataBundleActivity.class);
                intent.putExtra(PurchaseDataBundleFragment.MOBILE_NETWORK, "04");
                startActivity(intent);
            }
        });

        return dataBundleView;
    }

}