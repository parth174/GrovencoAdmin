package com.grovenco.grovencoadmin;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyFragmentAdapter extends FragmentPagerAdapter {

    private Context myContext;
    int totalTabs;
    private String[] tabTitles ;



    public MyFragmentAdapter(Context context, FragmentManager fm, int totalTabs, String[] tabTitles) {
        super(fm);
        myContext = context ;
        this.totalTabs = totalTabs ;
        this.tabTitles = tabTitles ;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PendingOrdersFragment() ;
            case 1:
                return  new DeliveredOrdersFragment() ;
            case 2:
                return  new RefundOrdersFragment() ;
            case 3:
                return  new CancelledOrdersFragment() ;
            default:
                return null;
        }
    }
    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }

}
