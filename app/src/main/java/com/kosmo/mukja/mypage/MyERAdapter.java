package com.kosmo.mukja.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MyERAdapter extends BaseAdapter {
    private ArrayList<MYERItem> mItems = new ArrayList<>();
    private ArrayList<String> erjoin_num_List = new ArrayList<>();
    private ArrayList<String> er_no_List = new ArrayList<>();
    private ArrayList<String> u_id_list = new ArrayList<>();
    private Context context;
    public MyERAdapter(){
    }

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
         context = parent.getContext();
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        String user_id = preferences.getString("username", "defaultID");
        /* 'listview_custom' Layout을 inflate하여 convertView 참조 획득 */
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.my_er_item, parent, false);
        }

        /* 'listview_custom'에 정의된 위젯에 대한 참조 획득 */


        TextView er_storename = convertView.findViewById(R.id.myer_storename) ;
        TextView er_title = convertView.findViewById(R.id.myer_title) ;
        TextView er_tend = convertView.findViewById(R.id.myer_tend) ;
        TextView er_max = convertView.findViewById(R.id.myer_max) ;
        TextView er_time = convertView.findViewById(R.id.myer_time) ;

        ImageView er_marsterimg =  convertView.findViewById(R.id.mater_img) ;
        TextView er_mastername = convertView.findViewById(R.id.mater_name) ;
        TextView er_matertend = convertView.findViewById(R.id.mater_tend) ;
        TextView er_materage = convertView.findViewById(R.id.master_age) ;

        TextView er_bey = convertView.findViewById(R.id.er_bey) ;

        TextView er_delete = convertView.findViewById(R.id.er_delete) ;

        if(u_id_list.get(position).equals(user_id)){
            er_bey.setVisibility(View.GONE);
            er_delete.setVisibility(View.VISIBLE);
            Log.i("MyMarker","방장아이디:"+u_id_list.get(position)+"내아이디:"+user_id);
        }
        if(!u_id_list.get(position).equals(user_id)){
            er_delete.setVisibility(View.GONE);
            er_bey.setVisibility(View.VISIBLE);
            Log.i("MyMarker","방장아이디:"+u_id_list.get(position)+"내아이디:"+user_id);
        }


        Log.i("MyMarker","어뎁터 아이템 길이:"+mItems.size());
        Log.i("MyMarker","어뎁터 유저이미지 경로 :"+mItems.get(position).getU_img());

        if(mItems.get(position).getU_img().length()!=0){
            Picasso.get().load(mItems.get(position).getU_img()).into(er_marsterimg);
        }

        er_storename.setText( mItems.get(position).getStore_name());
        er_title.setText( mItems.get(position).getEr_title());
        er_tend.setText( mItems.get(position).getEr_tend());
        er_max.setText( mItems.get(position).getEr_max());
        er_time.setText( mItems.get(position).getEr_time());

        er_mastername.setText( mItems.get(position).getU_nick());
        er_matertend.setText( mItems.get(position).getU_tend());
        er_materage.setText( mItems.get(position).getU_age());

        /* (위젯에 대한 이벤트리스너를 지정하고 싶다면 여기에 작성하면된다..)  */
        er_marsterimg.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            er_marsterimg.setClipToOutline(true);
        }

        er_bey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MyMarker"," 클릭시 발생 erjoin_num:"+ er_no_List.get(position));

                new deleteERMembers().execute("http://"+ TabContent2.ipAddr +"/mukja/Android/DeleteERMembers.do",er_no_List.get(position),user_id);
                Toast.makeText(context, "모임에서 탈퇴하셨어요!", Toast.LENGTH_SHORT).show();
                mItems.remove(position);
                notifyDataSetChanged();
            }//seton
        });
        er_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("MyMarker"," 클릭시 발생 erjoin_num:"+ er_no_List.get(position));
                new BoomMyER().execute("http://"+ TabContent2.ipAddr +"/mukja/Android/BoomMyER.do",er_no_List.get(position),user_id);

                Toast.makeText(context, "내 모임을 삭제 했어요!", Toast.LENGTH_SHORT).show();
                mItems.remove(position);
                notifyDataSetChanged();
            }//seton
        });
        return convertView;
    }

    /* 아이템 데이터 추가를 위한 함수. 자신이 원하는대로 작성 */
    public void addItem(  String er_no,
             String erjoin_num,
             String store_name,
             String er_title,
             String er_tend,
             String er_max,
             String er_time,
             String u_nick,
             String u_tend,
             String u_age,
             String u_img,
             String u_id) {
        Log.i("MyMarker"," 어뎁터 추가");
        Log.i("MyMarker"," erjoin_num:"+erjoin_num);
        MYERItem myerItem = new MYERItem();
        /* MyItem에 아이템을 setting한다. */

        myerItem.setEr_no(er_no);
        myerItem.setErjoin_num(erjoin_num);
        myerItem.setStore_name(store_name);
        myerItem.setEr_title(er_title);
        myerItem.setEr_tend(er_tend);
        myerItem.setEr_max(er_max);
        myerItem.setEr_time(er_time);
        myerItem.setU_nick(u_nick);
        myerItem.setU_tend(u_tend);
        myerItem.setU_age(u_age);
        myerItem.setU_img(u_img);
        myerItem.setU_id(u_id);
        mItems.add(myerItem);
        erjoin_num_List.add(erjoin_num);
        er_no_List.add(er_no);
        u_id_list.add(u_id);
    }//addItem

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class BoomMyER extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setIcon(android.R.drawable.btn_star_big_on);
            builder.setTitle("로딩");
            builder.setMessage("음식점을 검색 중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();
        }///////////onPreExecute
        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?er_no=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?er_no=%s&user_id=%s",params[0],params[1],params[2]));
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //서버에 요청 및 응답코드 받기
                int responseCode=conn.getResponseCode();
                if(responseCode ==HttpURLConnection.HTTP_OK){
                    //연결된 커넥션에서 서버에서 보낸 데이타 읽기
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line;
                    while((line=br.readLine())!=null){
                        buf.append(line);
                    }
                    br.close();
                }
            }
            catch(Exception e){e.printStackTrace();}

            SystemClock.sleep(2000);
            return buf.toString();
        }///////////doInBackground

        @Override
        protected void onPostExecute(String result) {

            Log.i("MyMarker","방나가기 결과:"+result);
            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class deleteERMembers extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setIcon(android.R.drawable.btn_star_big_on);
            builder.setTitle("로딩");
            builder.setMessage("음식점을 검색 중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();
        }///////////onPreExecute
        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?er_no=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?er_no=%s&user_id=%s",params[0],params[1],params[2]));
                HttpURLConnection conn=(HttpURLConnection)url.openConnection();
                //서버에 요청 및 응답코드 받기
                int responseCode=conn.getResponseCode();
                if(responseCode ==HttpURLConnection.HTTP_OK){
                    //연결된 커넥션에서 서버에서 보낸 데이타 읽기
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream(),"UTF-8"));
                    String line;
                    while((line=br.readLine())!=null){
                        buf.append(line);
                    }
                    br.close();
                }
            }
            catch(Exception e){e.printStackTrace();}

            SystemClock.sleep(2000);
            return buf.toString();
        }///////////doInBackground

        @Override
        protected void onPostExecute(String result) {

            Log.i("MyMarker","방나가기 결과:"+result);
            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask

}
