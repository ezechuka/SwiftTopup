package com.javalon.swifttopup;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;

public class MoreInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.swiftopuplogo)
                .setDescription(getResources().getString(R.string.more_info))
                .setCustomFont("product_sans_regular.ttf")
                .create();

        setContentView(aboutPage);
    }
}
