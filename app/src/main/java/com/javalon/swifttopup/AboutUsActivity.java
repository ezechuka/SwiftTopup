package com.javalon.swifttopup;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Element versionElement = new Element();
        versionElement.setTitle("Version 1.0");
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.drawable.swiftopuplogo)
                .setDescription(getResources().getString(R.string.description))
                .addItem(versionElement)
                .addGroup("Connect with us")
                .addEmail("swifttopup9@gmail.com")
                .addPlayStore("com.javalon.swifttopup")
                .setCustomFont("product_sans_regular.ttf")
                .create();

        setContentView(aboutPage);
    }
}