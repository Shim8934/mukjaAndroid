package com.kosmo.mukja.storeinfo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.review.StroeReview;
import com.kosmo.mukja.content.TabContent2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import pyxis.uzuki.live.rollingbanner.RollingBanner;
import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter;

public class Store_infoActivity extends AppCompatActivity {
    private TextView tvAddr;
    private TextView tvPh;
    private TextView tvEmail;
    private TextView tvTime;
    private TextView tvIntro;
    private TextView tvName;
    private TextView tvReview;
    private TextView tvFallow;
    private ImageView testIMG;
    private ImageView store_close;
    private RatingBar ratingStore;
    private ToggleButton likeToggle;
    public  Context context;
    private RollingBanner rollingBanner;
    private RollingAdapter adapter;
    private StroeInfo_Adapter stroeInfoAdapter;
    private ListView store_menu_listview;
    private String isThumb;

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.AT_MOST);

        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    private void initView() {
        tvAddr = (TextView) findViewById(R.id.tv_addr);
        tvPh = (TextView) findViewById(R.id.tv_ph);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvIntro = (TextView) findViewById(R.id.tv_intro);
        tvReview = (TextView) findViewById(R.id.tvReview);
        ratingStore = (RatingBar) findViewById(R.id.rating_store);
        likeToggle = (ToggleButton) findViewById(R.id.likeToggle);
        tvName = (TextView) findViewById(R.id.tv_name);
        rollingBanner = findViewById(R.id.banner);
        testIMG = findViewById(R.id.testIMG);
        store_close =findViewById(R.id.store_close);
        store_menu_listview = findViewById(R.id.store_menu_listview);
        tvFallow = findViewById(R.id.tv_fallow);
    }//initView

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_info);
        initView();
        context=Store_infoActivity.this;
        Intent intent = getIntent();
        String store_id = intent.getStringExtra("store_id");

        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        String user_id = preferences.getString("username", "defaultID");


        Log.i("MyMarker","스토어디테일 store_id:"+store_id);
        Log.i("MyMarker","스토어디테일 user_id:"+user_id);


        new GetStoreInfoAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/Andorid/Store/DetailView.do",store_id,user_id);

        likeToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(likeToggle.isChecked()){
                    likeToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.andorid_thumb_c));
                    new ThumbAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/updateStoreRecommand.do",store_id,user_id);
                }else{
                    likeToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.andorid_thumb_b));
                    new ThumbAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/updateStoreRecommand.do",store_id,user_id);
                }
            }//onClick
        });//setOnClickListener
        ratingStore.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                String starcount =String.valueOf((int)v);
                Log.i("MyMarker"," starcount:"+starcount);
                new RatingAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/updateStoreAvg.do",store_id,user_id,starcount);
            }
        });
        store_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tvReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent reviewIntent = new Intent(context, StroeReview.class);
                reviewIntent.putExtra("store_id",store_id);
                reviewIntent.putExtra("user_id",user_id);
                startActivity(reviewIntent);
            }
        });

        tvFallow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new FallowAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/Android/Fallow.do",store_id,user_id);
            }
        });

    }//onCreate

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class FallowAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;


        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
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
            JsonParser jsonParser = new JsonParser();
            JsonObject storeinfoJson = (JsonObject) jsonParser.parse(result);
            result = storeinfoJson.get("result").toString().replaceAll("\"","").trim();
            Log.i("MyMarker","찜결과:"+result);
            if(Integer.parseInt(result)==0 ){
                Toast.makeText(context,"이미 찜한 상태에요!",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context,"찜하였어요,마이페이지에서 확인가능해요!",Toast.LENGTH_LONG).show();
            }
            //다이얼로그 닫기

        }
    }///////////////AsyncTask


    public class RollingAdapter extends RollingViewPagerAdapter<String> {

        public RollingAdapter(Context context, ArrayList<String> itemList) {
            super(context, itemList);
        }

        @Override
        public View getView(int position, String item) {
            View view = LayoutInflater.from(context).inflate(R.layout.rolling_pager, null, false);
            FrameLayout container = view.findViewById(R.id.container);
            ImageView rolling = view.findViewById(R.id.rolling_img);

            Log.i("MyMarker","item:"+item);
            Glide.with(context).load(item).into(rolling);

            return view;
        }
    }


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class RatingAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("별점 로딩 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?store_id=%s&user_id=%s&starCount=%s",params[0],params[1],params[2],params[3]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?store_id=%s&user_id=%s&starCount=%s",params[0],params[1],params[2],params[3]));
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
            Log.i("MyMarker","추천result:"+result);

            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask





    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class ThumbAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("추천 로딩 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
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

            Log.i("MyMarker","추천result:"+result);

            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask



    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class GetStoreInfoAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("가게정보를 불러오는 중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?store_id=%s&user_id=%s",params[0],params[1],params[2]));
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
           JsonParser jsonParser = new JsonParser();
            JsonObject storeinfoJson = (JsonObject) jsonParser.parse(result);
            Log.i("MyMarker","storeinfoJson:"+storeinfoJson.toString());

            tvAddr.setText(storeinfoJson.get("store_addr").toString().replaceAll("\"",""));
            tvPh.setText(storeinfoJson.get("store_phnum").toString().replaceAll("\"",""));
            tvEmail .setText(storeinfoJson.get("store_email").toString().replaceAll("\"",""));
            tvTime.setText(storeinfoJson.get("store_time").toString().replaceAll("\"",""));
            tvIntro .setText(storeinfoJson.get("store_intro").toString().replaceAll("\"",""));
            tvName .setText(storeinfoJson.get("store_name").toString().replaceAll("\"",""));
            JsonArray imgJsonArray =  storeinfoJson.getAsJsonArray("store_imgList");
            ArrayList<String>  arrayList = new ArrayList<String>();
            for(int i=0;i<imgJsonArray.size();i++){
                arrayList.add("http://192.168.0.6:8080/mukja"+(imgJsonArray.get(i).toString().replaceAll("\"","")));
            }


            stroeInfoAdapter = new StroeInfo_Adapter();
            JsonArray menuJsonArray =  storeinfoJson.getAsJsonArray("menuList");
            Log.i("MyMarker","menuJsonArray.size():"+menuJsonArray.size());
            for(int i=0;i<menuJsonArray.size();i++){
                String menuName  =  menuJsonArray.get(i).getAsJsonObject().get("menuName").toString().replaceAll("\"","");
                String menuCost  =  menuJsonArray.get(i).getAsJsonObject().get("menuCost").toString().replaceAll("\"","")+"원";
                String menuInfo  =  menuJsonArray.get(i).getAsJsonObject().get("menuInfo").toString().replaceAll("\"","");
                String menuIntro  =  menuJsonArray.get(i).getAsJsonObject().get("menuIntro").toString().replaceAll("\"","");
                stroeInfoAdapter.addItem(menuName,menuCost,menuInfo,menuIntro);
                Log.i("MyMarker","들어간 내용:"+menuName+"/"+menuCost+"/"+menuInfo+"/"+menuIntro);
            }
            //메뉴 커스텀리스트뷰
            store_menu_listview.setAdapter(stroeInfoAdapter);
            setListViewHeightBasedOnChildren(store_menu_listview);
            //카라셀
            adapter = new RollingAdapter(context, arrayList);
            rollingBanner.setAdapter(adapter);
            //추천 초기화
            isThumb = storeinfoJson.get("isThumb").toString();
            Log.i("MyMarker","isThumb:"+isThumb);

            //추천을 한상태면 손가락이 붉은 색 아니면 회색
            if(Integer.parseInt(isThumb)==1){
                likeToggle.setChecked(true);
                likeToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.andorid_thumb_c));
            }else{
                likeToggle.setChecked(false);
                likeToggle.setBackgroundDrawable(getResources().getDrawable(R.drawable.andorid_thumb_b));

            }
            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask
}//class
