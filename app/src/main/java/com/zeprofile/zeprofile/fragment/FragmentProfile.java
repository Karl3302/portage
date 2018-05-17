package com.zeprofile.zeprofile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.zeprofile.zeprofile.R;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

public class FragmentProfile extends Fragment {
    //private String email;
    private TextView mDescriptionParamTextView;
    private Button mPublicProfileBtn, mVisibilityBtn, mBankAccountBtn, mUserSettingBtn, mAboutBtn;
    // Save the created fragment
    private View rootView;
    //private ViewPager mProfileRootViewPager;
    //private List<Fragment> listFragment;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_profile, container, false);//TODO false去掉会报错: Unable to start activity ComponentInfo{com.zeprofile.zeprofile/com.zeprofile.zeprofile.MainPage}: java.lang.IllegalStateException: The specified child already has a parent. You must call removeView() on the child's parent first.
        }
        initView();
        initData(inflater);
        configView();
        return rootView;
    }

    public void initView() {
        mDescriptionParamTextView = (TextView) rootView.findViewById(R.id.descriptionParamTextView);
        mPublicProfileBtn = (Button) rootView.findViewById(R.id.publicProfileBtn);
        mVisibilityBtn = (Button) rootView.findViewById(R.id.visibilityBtn);
        mBankAccountBtn = (Button) rootView.findViewById(R.id.bankAccountBtn);
        mUserSettingBtn = (Button) rootView.findViewById(R.id.userSettingBtn);
        mAboutBtn = (Button) rootView.findViewById(R.id.aboutBtn);
        // mProfileRootViewPager = (ViewPager) getActivity().findViewById(R.id.profileRootViewPager);
    }

    public void initData(final LayoutInflater inflater) {
        // Get the userEmail transferred by the last activity
        //email = ZeProfileUtils.getStringFromLastActivity((Activity) inflater.getContext(), "emailAddress");
    }

    public void configView() {
//        Window window = ((Activity)(inflater.getContext())).getWindow();
//        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
//        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//        window.setStatusBarColor(Color.TRANSPARENT);//设置状态栏颜色透
//        window.getWindow().setNavigationBarColor(Color.TRANSPARENT);//设置导航栏颜色透明

        // Set up click event for sign up button
        mPublicProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), FragmentPublicProfile.class.getSimpleName());
            }
        });
        mVisibilityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), PreferenceFragmentVisibility.class.getSimpleName());
            }
        });
        mBankAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), FragmentBankAccount.class.getSimpleName());
            }
        });
        mUserSettingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), PreferenceFragmentUserSettings.class.getSimpleName());
            }
        });
        mAboutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), FragmentAbout.class.getSimpleName());
            }
        });
    }
}
