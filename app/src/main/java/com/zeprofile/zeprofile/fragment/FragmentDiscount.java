package com.zeprofile.zeprofile.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeprofile.zeprofile.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentDiscount extends Fragment {
    private RecyclerView mRecyclerView;
    private List<String> mStringList= new ArrayList<>();
    // Save the created fragment
    private View rootView;

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
        return rootView;
    }

    public void initView(View view){
        mStringList.add("a");
        mStringList.add("b");
        mStringList.add("c");
        mStringList.add("d");
        mStringList.add("e");
        mStringList.add("f");

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycleView_FragmentDiscount);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
       // mRecyclerView.setAdapter(new CustomRecyclerViewAdapter(mStringList));
    }
}