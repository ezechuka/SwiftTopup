package com.javalon.swifttopup;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.javiersantos.materialstyleddialogs.MaterialStyledDialog;

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

        // setup listener
        dstv_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("Alert")
                        .setDescription("Not Available yet!")
                        .setPositiveText(R.string.ok)
                        .setHeaderDrawable(R.drawable.dstv_gotv_startimes)
                        .withIconAnimation(true)
                        .withDialogAnimation(true)
                        .withDivider(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        gotv_View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("Alert")
                        .setDescription("Not Available yet!")
                        .setPositiveText(R.string.ok)
                        .withIconAnimation(true)
                        .withDialogAnimation(true)
                        .withDivider(true)
                        .setHeaderDrawable(R.drawable.dstv_gotv_startimes)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        starTimesView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialStyledDialog.Builder(getContext())
                        .setTitle("Alert")
                        .setDescription("Not Available yet!")
                        .setPositiveText(R.string.ok)
                        .setHeaderDrawable(R.drawable.dstv_gotv_startimes)
                        .withIconAnimation(true)
                        .withDivider(true)
                        .withDialogAnimation(true)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });

        return cableTView;
    }
}