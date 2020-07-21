package com.javalon.swifttopup.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.javalon.swifttopup.AirtimeFragment;
import com.javalon.swifttopup.CableTVFragment;
import com.javalon.swifttopup.DataBundleFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private AirtimeFragment airtimeFragment = new AirtimeFragment();
    private DataBundleFragment dataBundleFragment = new DataBundleFragment();
    private CableTVFragment cableTVFragment = new CableTVFragment();

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
        }
        return null;
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}