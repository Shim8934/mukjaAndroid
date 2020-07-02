package com.kosmo.mukja;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter;

public class SampleAdapter extends RollingViewPagerAdapter<String> {
    private int[] colorRes = new int[]{0xff9C27B0, 0xff03A9F4, 0xff00BCD4, 0xff009688, 0xff4CAF50};

    public SampleAdapter(Context context, ArrayList<String> itemList) {
        super(context, itemList);
    }

    @Override
    public View getView(int position, String item) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.activity_main_pager, null, false);
        FrameLayout container = view.findViewById(R.id.container);
        TextView txtText = view.findViewById(R.id.txtText);

        String txt = getItem(position);
        int index = getItemList().indexOf(txt);
        txtText.setText(txt);
        container.setBackgroundColor(colorRes[index]);
        view.setOnClickListener(v ->
                Toast.makeText(getContext(), String.format("clicked %s", txt), Toast.LENGTH_SHORT).show());
        return view;
    }
}
