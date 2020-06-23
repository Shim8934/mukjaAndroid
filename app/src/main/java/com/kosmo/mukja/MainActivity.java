package com.kosmo.mukja;

import android.Manifest;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kosmo.mukja.content.TabContent1;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.content.TabContent3;

import java.util.List;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<Fragment> fragments= new Vector<Fragment>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //위젯 얻기]
        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        //탭 메뉴 추가
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_home).setText("홈"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_search_x).setText("식당검색"));
        tabLayout.addTab(tabLayout.newTab().setIcon(R.drawable.ic_mypage).setText("마이페이지"));
        //Fragment 생성후 컬렉션에 저장
        TabContent1 tabContent1= new TabContent1();
        fragments.add(tabContent1);
        TabContent2 tabContent2= new TabContent2();
        fragments.add(tabContent2);
        TabContent3 tabContent3= new TabContent3();
        fragments.add(tabContent3);
        //뷰 페이저를 관리하는 PageAdapter를 생성
        MyPagerAdapter myPagerAdapter= new MyPagerAdapter(getSupportFragmentManager(),fragments);
        //ViewPager에 PageAdapter를 연결
        viewPager.setAdapter(myPagerAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        //리스너 설정
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            //탭이 선택되었을 때 호출
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }
            //탭이 선택되지 않았을 때 호출
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }
            //탭이 다시 선택되었을 때 호출
            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }
}
