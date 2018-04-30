package com.zeprofile.zeprofile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeprofile.zeprofile.Utils.FragmentAdapter;
import com.zeprofile.zeprofile.Utils.ZeProfileUtils;

import java.util.ArrayList;
import java.util.List;

public class FragmentProfileRoot extends Fragment {
    private String email;
    private ViewPager mProfileRootViewPager;
    private View mView;
    private List<Fragment> listFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile_root, container, false);
        initView();
        initData(inflater);
        configView(inflater);
        return mView;
    }

    public void initView() {
        // Add the fragments to the viewpager
        listFragment = new ArrayList<>();
        listFragment.add(new FragmentProfile());
        listFragment.add(new FragmentProfilePublic());
        listFragment.add(new FragmentVisibility());
        listFragment.add(new FragmentBankAccount());
        listFragment.add(new FragmentUserSetting());
        listFragment.add(new FragmentFeedback());
        mProfileRootViewPager = (ViewPager) mView.findViewById(R.id.profileRootViewPager);
    }

    public void initData(LayoutInflater inflater) {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity((Activity) inflater.getContext(), "emailAddress");
    }

    public void configView(LayoutInflater inflater) {
        FragmentAdapter mAdapter = new FragmentAdapter(this.getChildFragmentManager(), this.getContext(), listFragment); // Need API 17 support
        mProfileRootViewPager.setAdapter(mAdapter);
        ZeProfileUtils.loadViewPager(getActivity(),R.id.mainMenuViewPager,0);
    }
}