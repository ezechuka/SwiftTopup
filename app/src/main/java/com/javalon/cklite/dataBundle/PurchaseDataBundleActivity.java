package com.javalon.cklite.dataBundle;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.javalon.cklite.R;
import com.javalon.cklite.airtime.PurchaseAirtimeFragment;

import co.paystack.android.PaystackSdk;

public class PurchaseDataBundleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_data_bundle);

        new PurchaseDataBundleFragment();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // initialize sdk
        PaystackSdk.initialize(getApplicationContext());
        PaystackSdk.setPublicKey("pk_test_ec7979fc5407a33159b00bfc1998319df18f9969");
    }
}
