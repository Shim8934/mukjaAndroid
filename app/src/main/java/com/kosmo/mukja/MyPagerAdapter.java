package com.kosmo.mukja;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.List;
import java.util.Vector;

//ViewPager와 연결해 Fragment를 관리하기 위한 어댑터
public class MyPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> fragments= new Vector<Fragment>();
    public MyPagerAdapter(FragmentManager fm, List<Fragment> fragments){
        super(fm, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.fragments = fragments;
    }
    //탭 메뉴의 position에 해당하는 프래그먼트를 반환
    @NonNull
    @Override
    public Fragment getItem(int position) {

        return fragments.get(position);
    }
    //page의 개수를 반환. fragments 크기가 page의 개수
    @Override
    public int getCount() {
        return fragments.size();
    }
}
