package com.zeprofile.application.test;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.annotation.Nullable;
import android.util.Log;

public class CustomPreferenceFragment extends PreferenceFragment {
    private String superClass;

    public static CustomPreferenceFragment newInstance(String inputClass){
        CustomPreferenceFragment fragment = new CustomPreferenceFragment();
        Bundle args =new Bundle();
        args.putString("superClass", inputClass);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState){
        Log.d("--- CustFrag ---","get in onCreate");
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if(args != null){
            superClass = args.getString("superClass");
            Log.d("--- CustFrag ---", "[CustomFrag creating] superclass = "+superClass);
        }
    }

    public String getSuperclass() {
        return superClass;
    }
}
