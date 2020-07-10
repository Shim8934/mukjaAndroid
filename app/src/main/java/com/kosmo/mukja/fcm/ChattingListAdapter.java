package com.kosmo.mukja.fcm;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosmo.mukja.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ChattingListAdapter extends BaseAdapter {

    private Context context;
    private List<ERDTO> items;
    private int layoutResId;
    private CreatERoomActivity customDialog;


    public ChattingListAdapter(Context context, List<ERDTO> items) {
        this.context = context;
        this.items = items;
    }

    public ChattingListAdapter(Context context, int layoutResId, List<ERDTO> items) {
        this.context = context;
        this.items = items;
        this.layoutResId = layoutResId;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_eroom_item, null);
        }
        ImageView itemIcon = convertView.findViewById(R.id.itemicon);
        Picasso.get().load(Uri.parse("http://115.91.88.230:9998/mukja" + items.get(position).getMaster_img())).into(itemIcon);
        final TextView textName = convertView.findViewById(R.id.itemTextName);
        textName.setText(" 제목: " + items.get(position).getEr_title());
        final TextView textDept = convertView.findViewById(R.id.itemTextDept);
        textDept.setText(" 내용: " + items.get(position).getEr_content());
        final TextView goin = convertView.findViewById(R.id.goin);
        goin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatDetailsActivity.class);
                intent.putExtra("er_no",items.get(position).getEr_no());
                intent.putExtra("store_id",items.get(position).getStore_id());
                intent.putExtra("master",items.get(position).getEr_master());
                intent.putExtra("img",items.get(position).getMaster_img());
                intent.putExtra("title",items.get(position).getEr_title());
                intent.putExtra("content",items.get(position).getEr_content());
                intent.putExtra("nick",items.get(position).getMaster_nick());
                intent.putExtra("age",items.get(position).getU_age());
                intent.putExtra("max",items.get(position).getEr_max());
                intent.putExtra("time",items.get(position).getEr_time());
                intent.putExtra("tend",items.get(position).getEr_tend());
                intent.putExtra("curr",items.get(position).getEr_curr());
                context.startActivity(intent);

            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,ChatActivity.class);
                intent.putExtra("username",items.get(position).getUsername());
                intent.putExtra("er_no",items.get(position).getEr_no());
                intent.putExtra("erc_no",items.get(position).getErc_no());
                intent.putExtra("nick",items.get(position).getMaster_nick());
                intent.putExtra("img",items.get(position).getMaster_img());
                context.startActivity(intent);
            }
        });
        return convertView;
    };


}/////////////
