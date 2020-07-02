package com.kosmo.mukja.content;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kosmo.mukja.MainActivity;
import com.kosmo.mukja.R;
import com.kosmo.mukja.SampleAdapter;

import java.util.ArrayList;
import java.util.Arrays;

import pyxis.uzuki.live.rollingbanner.RollingBanner;
import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent5 extends Fragment {
    //2]onCreateView()오버 라이딩
    private RollingBanner rollingBanner;
    private String[] txtRes = new String[]{"Purple", "Light Blue", "Cyan", "Teal", "Green"};
    private Context context;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_remain,container,false);
            rollingBanner = view.findViewById(R.id.banner);
        Log.i("dddd","여기까지옴");
            SampleAdapter adapter = new SampleAdapter(context, new ArrayList<>(Arrays.asList(txtRes)));
        Log.i("dddd","여기까지옴");
            rollingBanner.setAdapter(adapter);
            return view;
    }
}
