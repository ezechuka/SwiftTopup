package com.javalon.cklite.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.javalon.cklite.AirtimeFragment;
import com.javalon.cklite.CableTVFragment;
import com.javalon.cklite.DataBundleFragment;
import com.javalon.cklite.ElectricityFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private AirtimeFragment airtimeFragment = new AirtimeFragment();
    private DataBundleFragment dataBundleFragment = new DataBundleFragment();
    private CableTVFragment cableTVFragment = new CableTVFragment();
    private ElectricityFragment electricityFragment = new ElectricityFragment();

    public PagerAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return airtimeFragment;
            case 1:
                return dataBundleFragment;
            case 2:
                return cableTVFragment;
//            case 3:
//                return electricityFragment;
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}