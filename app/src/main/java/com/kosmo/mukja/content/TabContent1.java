package com.kosmo.mukja.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.kosmo.mukja.LoginActivity;
import com.kosmo.mukja.MainActivity;

import com.kosmo.mukja.R;
import com.kosmo.mukja.RegisterActivity;

import butterknife.BindView;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent1 extends Fragment {
    //2]onCreateView()오버 라이딩


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.tabmenu1_layout,null,false);
        return view;
        return inflater.inflate(R.layout.tabmenu1_layout,container,false);
    }
}
