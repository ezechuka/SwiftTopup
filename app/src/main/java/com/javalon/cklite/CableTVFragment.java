package com.javalon.cklite;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.javalon.cklite.cableTV.PurchaseCableTVActivity;
import com.javalon.cklite.cableTV.PurchaseCableTVFragment;

public class CableTVFragment extends Fragment {

    private CardView dstv_View;
    private CardView gotv_View;
    private CardView starTimesView;

    public CableTVFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View cableTView = inflater.inflate(R.layout.fragment_cable_tv, container, false);
        dstv_View = cableTView.findViewById(R.id.dstv_view);
        gotv_View = cableTView.findViewById(R.id.gotv_view);
        starTimesView = cableTView.findViewById(R.id.starTimes_view);

        // dstv click listener
        dstv_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent dstvIntent = new Intent(getActivity(), PurchaseCableTVActivity.class);
                dstvIntent.putExtra(PurchaseCableTVFragment.CABLE_TV, "01");
                startActivity(dstvIntent);
            }
        });

        // gotv click listener
        gotv_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotvIntent = new Intent(getActivity(), PurchaseCableTVActivity.class);
                gotvIntent.putExtra(PurchaseCableTVFragment.CABLE_TV, "02");
                startActivity(gotvIntent);
            }
        });

        // starTimes click listener
        starTimesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotvIntent = new Intent(getActivity(), PurchaseCableTVActivity.class);
                gotvIntent.putExtra(PurchaseCableTVFragment.CABLE_TV, "03");
                startActivity(gotvIntent);
            }
        });

        return cableTView;
    }
}