package com.kosmo.mukja.sns;

import android.content.Context;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosmo.mukja.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
public class SNSAdapter extends BaseAdapter {
    private ArrayList<SNSItem> mItems = new ArrayList<>();
    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public Object getItem(int i) {
        return mItems.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.snsitem, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        ImageView snsProfile =  convertView.findViewById(R.id.img_snsProfile) ;
        TextView snsId = convertView.findViewById(R.id.tv_snsId) ;
        ImageView snsContent =  convertView.findViewById(R.id.img_snsContent) ;
        TextView snsRe = convertView.findViewById(R.id.tv_snsRe) ;

        /* 각 리스트에 뿌려줄 아이템을 받아오는데 mMyItem 재활용 */
        SNSItem snsItem = (SNSItem) getItem(position);

        /* 각 위젯에 세팅된 아이템을 뿌려준다 */
        snsItem.setSnsProfile(snsItem.getSnsProfile());
        snsItem.setSnsID(snsItem.getSnsID());
        snsItem.setSnsContent(snsItem.getSnsContent());
        snsItem.setSnsRe(snsItem.getSnsRe());
/*
        Glide.with(context).load(mItems.get(position).getSnsProfile()).into(snsProfile);
        Glide.with(context).load(mItems.get(position).getSnsContent()).into(snsContent);*/

        Picasso.get().load(mItems.get(position).getSnsProfile()).into(snsProfile);
        Picasso.get().load(mItems.get(position).getSnsContent()).into(snsContent);

        snsId.setText( mItems.get(position).getSnsID());
        snsRe.setText( mItems.get(position).getSnsRe());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        snsProfile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            snsProfile.setClipToOutline(true);
        }


        return convertView;
    }
    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String snsProfile, String snsId, String snsContent, String snsRe) {
        Log.i("MyMarker"," 어뎁터 추가");
        SNSItem snsItem = new SNSItem();
        /* MyItem에 아이템을 setting한다. */
        snsItem.setSnsProfile(snsProfile);
        snsItem.setSnsID(snsId);
        snsItem.setSnsContent(snsContent);
        snsItem.setSnsRe(snsRe);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(snsItem);

    }
}
