package com.zeprofile.zeprofile.test;

import android.content.Context;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;

import java.util.List;

public class FragmentAdapter extends FragmentStatePagerAdapter {
    private Context context;
    private List<Fragment> listFragment;

    public FragmentAdapter(FragmentManager fm, Context context, List<Fragment> listFragment) {
        super(fm);
        this.context = context;
        this.listFragment = listFragment;
    }

    @Override
    public Fragment getItem(int position) {

        return listFragment.get(position);
    }

    @Override
    public int getCount() {
        return listFragment.size();
    }
}
