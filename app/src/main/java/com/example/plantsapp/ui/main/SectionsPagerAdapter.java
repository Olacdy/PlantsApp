package com.example.plantsapp.ui.main;

import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SectionsPagerAdapter extends FragmentPagerAdapter {
    public SectionsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new AllPlantsFragment();
            case 1:
                return new DeadPlantsFragment();
            default:
                return null;
        }
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        AllPlantsFragment allPlantsFragment;
        DeadPlantsFragment deadPlantsFragment;
        switch (position) {
            case 0:
                allPlantsFragment = (AllPlantsFragment) createdFragment;
                break;
            case 1:
                deadPlantsFragment = (DeadPlantsFragment) createdFragment;
                break;
        }
        return createdFragment;
    }

    public int getCount(){
        return 2;
    }
}