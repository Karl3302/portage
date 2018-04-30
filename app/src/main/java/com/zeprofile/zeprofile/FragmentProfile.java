package com.zeprofile.zeprofile;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zeprofile.zeprofile.Utils.ZeProfileUtils;

import java.util.List;

public class FragmentProfile extends Fragment {
    private String email;
    private TextView mDescriptionParamTextView;
    private Button mProfilePublicBtn, mVisibilityBtn, mBankAccountBtn, mUserSettingBtn, mSuggestionBtn;
    private View mView;
    private ViewPager mProfileRootViewPager;
    private List<Fragment> listFragment;

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_profile, container, false);

        initView();
        initData(inflater);
        configView(inflater);
        return mView;
    }

    public void initView() {
        mDescriptionParamTextView = (TextView) mView.findViewById(R.id.descriptionParamTextView);
        mProfilePublicBtn = (Button) mView.findViewById(R.id.profilePublicBtn);
        mVisibilityBtn = (Button) mView.findViewById(R.id.visibilityBtn);
        mBankAccountBtn = (Button) mView.findViewById(R.id.bankAccountBtn);
        mUserSettingBtn = (Button) mView.findViewById(R.id.userSettingBtn);
        mSuggestionBtn = (Button) mView.findViewById(R.id.suggestionBtn);

        mProfileRootViewPager = (ViewPager) getActivity().findViewById(R.id.profileRootViewPager);
    }

    public void initData(final LayoutInflater inflater) {
        // Get the userEmail transferred by the last activity
        email = ZeProfileUtils.getStringFromLastActivity((Activity) inflater.getContext(), "emailAddress");
    }

    public void configView(final LayoutInflater inflater) {
        /*Window window = ((Activity)(inflater.getContext())).getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色透
        window.getWindow().setNavigationBarColor(Color.TRANSPARENT);//设置导航栏颜色透明*/

        // Set up click event for sign up button
        mProfilePublicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                //mProfileRootViewPager.setCurrentItem(1,false);
                ZeProfileUtils.loadViewPager(getActivity(), R.id.profileRootViewPager,1);
                //ZeProfileUtils.moveToNextActivity(inflater.getContext(), ManageProfilePublic.class, "emailAddress", email);
            }
        });
        mVisibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadViewPager(getActivity(), R.id.profileRootViewPager,2);
            }
        });
        mBankAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadViewPager(getActivity(), R.id.profileRootViewPager,3);
            }
        });
        mUserSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadViewPager(getActivity(), R.id.profileRootViewPager,4);
            }
        });
        mSuggestionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadViewPager(getActivity(), R.id.profileRootViewPager,5);
            }
        });
    }
}
