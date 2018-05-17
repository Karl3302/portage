package com.zeprofile.zeprofile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.zeprofile.zeprofile.R;
import com.zeprofile.zeprofile.utils.ZeProfileUtils;

public class FragmentBankAccount extends Fragment {
    // Save the created fragment
    private View rootView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_bank_account, container, false);
        }
        setHasOptionsMenu(true);
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