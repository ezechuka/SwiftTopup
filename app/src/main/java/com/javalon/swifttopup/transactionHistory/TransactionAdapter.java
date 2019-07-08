package com.javalon.swifttopup.transactionHistory;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.javalon.swifttopup.QueryTransact;
import com.javalon.swifttopup.R;

import java.util.ArrayList;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {
    private ArrayList<QueryTransact> log;
    private Context context;
    private int[] networkIcons = {
            R.drawable.mtn_logo_1,
            R.drawable.glo,
            R.drawable.nine_mobile_logo_1,
            R.drawable.airtel_logo_2
    };

    public TransactionAdapter(ArrayList<QueryTransact> log, Context context) {
        this.log = log;
        this.context = context;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        ViewHolder(CardView cv) {
            super(cv);
            this.cardView = cv;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardView cardView = (CardView)
                LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_log, parent, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CardView cv = holder.cardView;
        final QueryTransact queryTransact = log.get(position);
        TextView purchaseDate = cv.findViewById(R.id.purchase_date);
        TextView purchaseInfo = cv.findViewById(R.id.purchase_info);
        ImageView networkIcon = cv.findViewById(R.id.network_icon);

        // populate cardview children
        purchaseDate.setText(queryTransact.getDate());
        purchaseInfo.setText(queryTransact.getMobilenetwork() + " - " + queryTransact.getOrdertype());
        networkIcon.setImageResource(networkIcons[Integer.parseInt(queryTransact.getNetworkIcon()) - 1]);

        // set listener for each card view log
        cv.setOnClickListener(

                new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder detailsBuilder = new StringBuilder();
                detailsBuilder.append(Html.fromHtml("<b>ID:</b>\t")).append(queryTransact.getOrderid()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Reference:</b>\t")).append(queryTransact.getTransactionReference()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Time:</b>\t")).append(queryTransact.getTime()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Date:</b>\t")).append(queryTransact.getDate()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Amount:</b>\t")).append(queryTransact.getOrdertype()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Status:<b>\t")).append(queryTransact.getStatus()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Mobile network:</b>\t")).append(queryTransact.getMobilenetwork()).append("\n");
                detailsBuilder.append(Html.fromHtml("<b>Mobile number:</b>\t")).append(queryTransact.getMobilenumber());

                AlertDialog.Builder detailDialog = new AlertDialog.Builder(context);
                detailDialog.setTitle("Transaction Details");
                detailDialog.setMessage(detailsBuilder.toString());
                detailDialog.setPositiveButton("GOT IT!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

                detailDialog.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return log.size();
    }
}