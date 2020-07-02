package com.kosmo.mukja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter;

public class SampleAdapter extends RollingViewPagerAdapter<String> {

    public SampleAdapter(Context context, ArrayList<String> itemList) {
        super(context, itemList);
    }

    @Override
    public View getView(int position, String item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_pager, null, false);
        FrameLayout container = view.findViewById(R.id.container);
        ImageView mainStImg = view.findViewById(R.id.mainStrImg);


        view.setOnClickListener(v ->
                Toast.makeText(getContext(), String.format("clicked %s", mainStImg), Toast.LENGTH_SHORT).show());
        return view;
    }
}
