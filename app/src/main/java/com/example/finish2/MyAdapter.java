package com.example.finish2;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class MyAdapter extends FragmentPagerAdapter
{
    public MyAdapter(@NonNull FragmentManager fm){
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new onBoardingOne();
            case 1:
                return new onBoardingTwo();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
