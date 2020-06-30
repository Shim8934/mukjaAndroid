package com.kosmo.mukja.content;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


import com.kosmo.mukja.R;

import butterknife.ButterKnife;

//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent1 extends Fragment {


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_eroom_item,container,false);
        return view;

    }


}
