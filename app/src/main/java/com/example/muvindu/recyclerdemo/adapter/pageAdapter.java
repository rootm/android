package com.example.muvindu.recyclerdemo.adapter;

import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.muvindu.recyclerdemo.Interface.recInterface;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Muvindu on 12/8/2016.
 */

public class pageAdapter extends FragmentPagerAdapter {



    private final ArrayList<Fragment> fragmentList= new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public pageAdapter(FragmentManager manager){
        super(manager);

    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();

    }

    public void addFragment(Fragment fragment,String title){
        fragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mFragmentTitleList.get(position);
    }
}
