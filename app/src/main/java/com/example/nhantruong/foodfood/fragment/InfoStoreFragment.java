package com.example.nhantruong.foodfood.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nhantruong.foodfood.R;
import com.example.nhantruong.foodfood.model.FoodOfMenu;

import java.util.ArrayList;

public class InfoStoreFragment extends Fragment {
    private View mRootView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_info_store,container,false);


        return mRootView;
    }
}
