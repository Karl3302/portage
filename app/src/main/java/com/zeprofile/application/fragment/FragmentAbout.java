package com.zeprofile.application.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeprofile.application.R;
import com.zeprofile.application.utils.ZeProfileUtils;

public class FragmentAbout extends Fragment {
    // Save the created fragment
    private View rootView;

    // debug
    private Button goToSettingsBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设备旋转时保存Fragment的交互状态(activity重建时,fragment不执行onCreate和onDestroy)
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_about, container, false);
        }

        setHasOptionsMenu(true);

        //debug
        goToSettingsBtn = (Button) rootView.findViewById(R.id.goToSettingsBtn);
        goToSettingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZeProfileUtils.loadMainFrame(getActivity(), PreferenceFragmentUserSettings.class.getSimpleName());
            }
        });
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            ZeProfileUtils.loadMainFrame(getActivity(),FragmentProfile.class.getSimpleName());
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}