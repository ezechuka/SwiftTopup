package com.javalon.swifttopup;

import android.content.Intent;
import android.os.Bundle;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.javalon.swifttopup.adapters.PagerAdapter;
import com.javalon.swifttopup.transactionHistory.TransactionHistoryActivity;

public class MainActivity extends AppCompatActivity {

    private ViewPager overviewPager;
    private TabLayout menuItemTabs;
    private FloatingActionButton cardDetails;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize viewpager and tablayout
        overviewPager = findViewById(R.id.overview_viewpager);
        menuItemTabs = findViewById(R.id.appbar_menu_items);

        // add tab items
        // airtime tab
        TabLayout.Tab airtimeTab = menuItemTabs.newTab();
        airtimeTab.setText(Html.fromHtml("<b>AIRTIME</b>"));
        airtimeTab.setIcon(R.drawable.airtime);
        menuItemTabs.addTab(airtimeTab);

        // data bundle tab
        TabLayout.Tab dataBundleTab = menuItemTabs.newTab();
        dataBundleTab.setText(Html.fromHtml("<b>DATA</b>"));
        dataBundleTab.setIcon(R.drawable.internet);
        menuItemTabs.addTab(dataBundleTab);

        // cable tv tab
        TabLayout.Tab cabletvTab = menuItemTabs.newTab();
        cabletvTab.setText(Html.fromHtml("<b>CABLE TV</b>"));
        cabletvTab.setIcon(R.drawable.cable_tv);
        menuItemTabs.addTab(cabletvTab);

        // initialize coordinatorLayout
        coordinatorLayout = findViewById(R.id.app_ui);

        // set listener on tabs in tab layout
        menuItemTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()) {
                    case 0:
                        overviewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 1:
                        overviewPager.setCurrentItem(tab.getPosition());
                        break;
                    case 2:
                        overviewPager.setCurrentItem(tab.getPosition());
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), menuItemTabs.getTabCount());
        overviewPager.setAdapter(pagerAdapter);
        overviewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(menuItemTabs));

        // initialize floatingActionButton
        cardDetails = findViewById(R.id.card_details);
        cardDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, CardDetailsActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.transact_history) {
            startActivity(new Intent(this, TransactionHistoryActivity.class));
            return true;
        } else if (id == R.id.about_us) {
            startActivity(new Intent(this, AboutUsActivity.class));
            return true;
        } else if (id == R.id.more_info) {
            startActivity(new Intent(this, MoreInfoActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}