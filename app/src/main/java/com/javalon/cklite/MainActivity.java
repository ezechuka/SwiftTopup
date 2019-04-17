package com.javalon.cklite;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.javalon.cklite.adapters.PagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager overviewPager;
    private TabLayout menuItemTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // initialize infoTextView
        TextView infoTextView = findViewById(R.id.infoTextView);
        infoTextView.setSelected(true);

        // initialize viewpager and tablayout
        overviewPager = findViewById(R.id.overview_viewpager);
        menuItemTabs = findViewById(R.id.appbar_menu_items);

        // add tab items
        // airtime tab
        TabLayout.Tab airtimeTab = menuItemTabs.newTab();
        airtimeTab.setText("AIRTIME");
        airtimeTab.setIcon(R.drawable.airtime);
        menuItemTabs.addTab(airtimeTab);

        // data bundle tab
        TabLayout.Tab dataBundleTab = menuItemTabs.newTab();
        dataBundleTab.setText("DATA BUNDLES");
        dataBundleTab.setIcon(R.drawable.internet);
        menuItemTabs.addTab(dataBundleTab);

        // cable tv tab
        TabLayout.Tab cabletvTab = menuItemTabs.newTab();
        cabletvTab.setText("CABLE TV");
        cabletvTab.setIcon(R.drawable.cable_tv);
        menuItemTabs.addTab(cabletvTab);

        // electricity tab
//        TabLayout.Tab electricTab = menuItemTabs.newTab();
//        electricTab.setText("ELECTRICITY");
//        electricTab.setIcon(R.drawable.electricity);
//        menuItemTabs.addTab(electricTab);

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
//                    case 3:
//                        overviewPager.setCurrentItem(tab.getPosition());
//                        break;
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}