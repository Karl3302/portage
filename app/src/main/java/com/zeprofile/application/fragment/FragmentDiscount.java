package com.zeprofile.application.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.zeprofile.application.MainPage;
import com.zeprofile.application.R;
import com.zeprofile.application.utils.ApiZeprofile;
import com.zeprofile.application.utils.RetrofitBuilder;
import com.zeprofile.application.utils.ZeProfileUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class FragmentDiscount extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mStringList= new ArrayList<>();
    // Save the created fragment
    private View rootView;
    private Button button_test;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设备旋转时保存Fragment的交互状态(activity重建时,fragment不执行onCreate和onDestroy)
        // Maintain the current instance when the screen orientation has changed
        setRetainInstance(true);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        if(rootView ==null){
            rootView = inflater.inflate(R.layout.fragment_discount, container, false);
        }

        initView(rootView);
        initData();
        configView();
        return rootView;
    }

    public void initView(View view){
        /*
        mStringList.add("a");
        mStringList.add("b");
        mStringList.add("c");
        mStringList.add("d");
        mStringList.add("e");
        mStringList.add("f");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleView_FragmentDiscount);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
       // mRecyclerView.setAdapter(new CustomRecyclerViewAdapter(mStringList));
       */
        button_test = (Button) rootView.findViewById(R.id.button_test);

    }

    public void initData() {
    }

    public void configView(){


        button_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*
                String fragName = PreferenceFragmentUserSettings.class.getSimpleName();
                Log.d("--- test ---","[getSuperClass] superClass of "+fragName+" is "+ZeProfileUtils.getSuperClass(fragName));
                */
            }
        });
    }

}