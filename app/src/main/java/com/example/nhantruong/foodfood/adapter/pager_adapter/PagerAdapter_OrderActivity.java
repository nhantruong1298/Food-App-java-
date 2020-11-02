package com.example.nhantruong.foodfood.adapter.pager_adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.nhantruong.foodfood.fragment.InfoStoreFragment;
import com.example.nhantruong.foodfood.fragment.MenuOrderFragment;

public class PagerAdapter_OrderActivity extends FragmentPagerAdapter {

    public InfoStoreFragment infoStoreFragment;
    public MenuOrderFragment menuOrderFragment;
    public PagerAdapter_OrderActivity(FragmentManager fm,InfoStoreFragment infoStoreFragment,MenuOrderFragment menuOrderFragment) {
        super(fm);
        this.infoStoreFragment = infoStoreFragment;
        this.menuOrderFragment = menuOrderFragment;
    }

    @Override
    public Fragment getItem(int i) {
        if(i == 0)
        {
            return menuOrderFragment;
        }
        else if(i ==1)
        {
            return infoStoreFragment;
        }
        else
        {

        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int i) {

        if(i == 0)
        {
            return "Menu";
        }
        else if(i ==1)
        {
            return "Thông tin";
        }
        else
        {

        }
        return null;
    }
}
