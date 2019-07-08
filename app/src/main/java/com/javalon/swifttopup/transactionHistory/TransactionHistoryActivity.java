package com.javalon.swifttopup.transactionHistory;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.javalon.swifttopup.Constants;
import com.javalon.swifttopup.QueryTransact;
import com.javalon.swifttopup.R;

import java.util.ArrayList;

import io.paperdb.Paper;

public class TransactionHistoryActivity extends AppCompatActivity {

    private static ArrayList<QueryTransact> queryLogs;
    private TransactionAdapter transactionAdapter;
    private RecyclerView transactionLogs;
    private TextView noTransact;
    private RelativeLayout parentViewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_history);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Paper.init(getApplicationContext());

        parentViewGroup = findViewById(R.id.transact_history);
        transactionLogs = findViewById(R.id.transaction_logs);
        noTransact = findViewById(R.id.no_transact);

        // read query logs from internal memory
        queryLogs = Paper.book().read(Constants.QUERY_LOGS, new ArrayList<QueryTransact>()); // set default value to arraylist of empty querylogs
        if (queryLogs.size() == 0) {
            noTransact.setVisibility(View.VISIBLE);
            transactionLogs.setVisibility(View.GONE);
        } else {
            noTransact.setVisibility(View.GONE);
            transactionLogs.setVisibility(View.VISIBLE);
        }

        transactionAdapter = new TransactionAdapter(queryLogs, TransactionHistoryActivity.this);
        transactionLogs.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        transactionLogs.setAdapter(transactionAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_transaction, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.clear_transact_history) {
            AlertDialog.Builder clearDialog = new AlertDialog.Builder(TransactionHistoryActivity.this);
            clearDialog.setTitle("Alert");
            clearDialog.setMessage("Do you want to clear all transaction history?");
            clearDialog.setCancelable(true);
            clearDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
            clearDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    queryLogs.clear();
                    Paper.book().delete(Constants.QUERY_LOGS);

                    // alert transaction adapter
                    transactionAdapter.notifyDataSetChanged();

                    if (queryLogs.size() == 0) {
                        noTransact.setVisibility(View.VISIBLE);
                        TransitionManager.beginDelayedTransition(parentViewGroup, null);
                    }
                }
            }).show();

        }
        return super.onOptionsItemSelected(item);
    }
}