package com.kosmo.mukja.storeinfo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kosmo.mukja.R;

import java.util.ArrayList;

public class StroeInfo_Adapter extends BaseAdapter {

    private ArrayList<StroeInfo_item> mItems = new ArrayList<>();
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
            convertView = inflater.inflate(R.layout.storeinfo_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */
        TextView menuName =  convertView.findViewById(R.id.menuName) ;
        TextView menuCost = convertView.findViewById(R.id.menuCost) ;
        TextView menuInfo =  convertView.findViewById(R.id.menuInfo) ;
        TextView menuIntro = convertView.findViewById(R.id.menuIntro) ;



        menuName.setText( mItems.get(position).getMenuName());
        menuCost.setText( mItems.get(position).getMenuCost());
        menuInfo.setText( mItems.get(position).getMenuInfo());
        menuIntro.setText( mItems.get(position).getMenuIntro());


        return convertView;
    }
    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(String menuName, String menuCost, String menuInfo, String menuIntro) {

        StroeInfo_item stroeInfo_item = new StroeInfo_item();
        /* MyItem에 아이템을 setting한다. */
        stroeInfo_item.setMenuName(menuName);
        stroeInfo_item.setMenuCost(menuCost);
        stroeInfo_item.setMenuInfo(menuInfo);
        stroeInfo_item.setMenuIntro(menuIntro);

        /* mItems에 MyItem을 추가한다. */
        mItems.add(stroeInfo_item);

    }
}
