package com.zeprofile.application.test;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;

public class CustomFragment extends Fragment {
    private String superClass;

    public static CustomFragment newInstance(String inputClass){
        CustomFragment fragment = new CustomFragment();
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
