package com.kosmo.mukja.fcm;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kosmo.mukja.R;
import com.squareup.picasso.Picasso;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;

import com.kosmo.mukja.LoginActivity;
import com.kosmo.mukja.R;
import com.kosmo.mukja.RegisterActivity;
import com.kosmo.mukja.fcm.ERDTO;

import java.util.List;

//1]BaseAdapter상속-4개의 메소드 오버라이딩
public class EroomAdapter extends BaseAdapter {

    //생성자를 통해서 초기화 할 멤버 변수들]

    //리스트뷰가 뿌려지는 컨텍스트
    private Context context;
    //리스트뷰에 뿌릴 데이타
    private List<ERDTO> items;
    //레이아웃 리소스 아이디(선택사항)
    private int layoutResId;
    private CreatERoomActivity customDialog;


    //2]생성자 정의:생성자로 Context와 리스트뷰에 뿌려줄 데이타를 받는다.
    //             리소스 레이아웃 아이디(int)는 선택사항
    //인자생성자1]컨텍스트와 데이타
    public EroomAdapter(Context context, List<ERDTO> items) {
        this.context = context;
        this.items = items;
    }

    public EroomAdapter(Context context, int layoutResId, List<ERDTO> items) {
        this.context = context;
        this.items = items;
        this.layoutResId = layoutResId;
    }

    //※리스트뷰에 의해서 호출됨.
    //아이템의 총 갯수 반환,
    //리스트뷰는 아이템 총 갯수만큼 아래 의 getView()메소드를 호출한다.
    @Override
    public int getCount() {
        return items.size();
    }

    //position에 해당하는 아이템 반환
    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    //아이템의 아이디 반환
    @Override
    public long getItemId(int position) {
        return position;
    }

    /*리스트뷰가 각 항목을 출력할때 어댑터의 getView()메소드 호출
    즉 출력할 아이템의 뷰를 생성해서 리스트 뷰에 반환
    convertview는 리스트 뷰의 각 항목을 나타내는 뷰이고
    parent는 부모 뷰 즉 리스트뷰가 됨.
   리스트뷰는 getView()호출시 최초 요청일때는 position이 0이고 convertView는 null이다
   두 번째 아이템 항목부터는 convertView레이아웃이 그대로 리스트뷰로부터 전달됨으로
   항목을 구성하는 위젯만 변경해서(리스트뷰로부터 전달받은 position값에 따라)  convertView를
   리스트뷰에 반환한다.
 */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //[리스트뷰에서 최초 아이템 요청시-getView()호출시]
            //※MainActivity 에서는 getLayoutInflayter()메소드 호출로
            //LayoutInflater을 얻을 수 있다.
            //Adapter 계열을 상속 받았기때문에
            //Context객체로 시스템서비스로 얻어야 한다.
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //인플레이트시 뷰그룹은 반드시 null지정
            //인자생성자1 사용시]
            convertView = inflater.inflate(R.layout.activity_eroom_item, null);
            //인자생성자2 사용시]
            //convertView=inflater.inflate(layoutResId,null);

        }
        //리스트 뷰에서 하나의 아이템을 구성하는 각 위젯의 데이타 설정]
        //이미지뷰 위젯 얻고 데이타 설정]

        ImageView itemIcon = convertView.findViewById(R.id.itemicon);
        Picasso.get().load(Uri.parse("http://115.91.88.230:9998/mukja" + items.get(position).getU_img())).into(itemIcon);
        //텍스트뷰 위젯 얻고 데이터 설정]
        final TextView textName = convertView.findViewById(R.id.itemTextName);
        textName.setText(" 제목: " + items.get(position).getEr_title());
        final TextView textDept = convertView.findViewById(R.id.itemTextDept);
        textDept.setText(" 내용: " + items.get(position).getEr_content());
        final TextView goin = convertView.findViewById(R.id.goin);
        goin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context,ViewDetailsActivity.class);
                intent.putExtra("no",items.get(position).getEr_no());
                intent.putExtra("master",items.get(position).getEr_master());
                intent.putExtra("img",items.get(position).getU_img());
                intent.putExtra("title",items.get(position).getEr_title());
                intent.putExtra("content",items.get(position).getEr_content());
                intent.putExtra("nick",items.get(position).getU_nick());
                intent.putExtra("age",items.get(position).getU_age());
                intent.putExtra("max",items.get(position).getEr_max());
                intent.putExtra("time",items.get(position).getEr_time());
                intent.putExtra("tend",items.get(position).getEr_tend());
                intent.putExtra("curr",items.get(position).getEr_curr());
                context.startActivity(intent);

            }
        });
        return convertView;
    };


}/////////////
