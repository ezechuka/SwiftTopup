package com.javalon.cklite.airtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.javalon.cklite.R;

import co.paystack.android.PaystackSdk;

public class PurchaseAirtimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_airtime);

        new PurchaseAirtimeFragment();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize sdk
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey("pk_test_ec7979fc5407a33159b00bfc1998319df18f9969");
    }
}
