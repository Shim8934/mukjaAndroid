package com.kosmo.mukja.fcm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EroomlistActivity extends AppCompatActivity {

    @BindView(R.id.room_list)
    ListView room_list;
    @BindView(R.id.creatRoom_btn)
    Button ctRoom_btn;
    @BindView(R.id.close_btn)
    Button close_btn;
    private Context context;
    private String store_id;
    private EroomAdapter eroomAdapter;
    private ArrayList<ERDTO> arrayList = new ArrayList<ERDTO>();
    private ERDTO erdto;
    private Bundle avoid_codes = new Bundle();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activitiy_eroom_list);
        ButterKnife.bind(this);
        EroomAsyncTask asyncTask = new EroomAsyncTask();
        asyncTask.execute();
        //tabContent2 fragment에서 eroom_list(모임리스트) 버튼 클릭시 intent를 넘긴걸 받아온다.
        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id").replaceAll("\"", "");
        ctRoom_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent creatRoom = new Intent(getApplicationContext(),CreatERoomActivity.class);
                creatRoom.putExtra("store_id",store_id);
                creatRoom.putExtra("avoid_codes",avoid_codes);
                startActivityForResult(creatRoom,3000);
            }
        });


        //모임리스트 닫기 버튼 시작
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//모임리스트 닫기 버튼 끝

    }/////onCreate

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class EroomAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("http://115.91.88.230:9998/mukja/eat_together_list.do?store_id=%s", store_id));
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                //서버에 요청 및 응답코드 받기
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    //연결된 커넥션에서 서버에서 보낸 데이타 읽기
                    BufferedReader br =
                            new BufferedReader(
                                    new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String line;
                    while ((line = br.readLine()) != null) {
                        buf.append(line);
                    }
                    br.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            SystemClock.sleep(2000);
            return buf.toString();
        }///////////doInBackground

        @Override
        protected void onPostExecute(String result) {
            Log.i("ERoom", result);
            if(result!=null) {
                JsonParser jsonParser = new JsonParser();
                JsonArray erooms = (JsonArray) jsonParser.parse(result);
                for (int i = 0; i < erooms.size(); i++) {
                    JsonObject eroomInfo = (JsonObject) erooms.get(i);
                    ERDTO erdto = new ERDTO();
                    erdto.setEr_no(Integer.parseInt(eroomInfo.get("er_no").toString().replaceAll("\"", "")));
                    erdto.setStore_id(eroomInfo.get("s_username").toString().replaceAll("\"", ""));
                    erdto.setU_img(eroomInfo.get("u_img").toString().replaceAll("\"", ""));
                    erdto.setEr_title(eroomInfo.get("er_title").toString().replaceAll("\"", ""));
                    erdto.setEr_content(eroomInfo.get("er_content").toString().replaceAll("\"", "").replace("<p>", "").replace("</p>", ""));
                    erdto.setU_age(eroomInfo.get("u_age").toString().replaceAll("\"", ""));
                    erdto.setU_nick(eroomInfo.get("u_nick").toString().replaceAll("\"", ""));
                    erdto.setEr_time(eroomInfo.get("er_time").toString().replaceAll("\"", ""));
                    erdto.setEr_master(eroomInfo.get("er_master").toString().replaceAll("\"", ""));
                    erdto.setEr_tend(eroomInfo.get("er_tend").toString().replaceAll("\"", ""));
                    erdto.setEr_max(eroomInfo.get("er_max").toString().replaceAll("\"", ""));
                    arrayList.add(erdto);
                }
                showChatList();
            }
        }

    }///////////////AsyncTask
    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        eroomAdapter = new EroomAdapter((Activity)EroomlistActivity.this, arrayList);
        room_list.setAdapter(eroomAdapter);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        arrayList = new ArrayList<ERDTO>();
        Log.i("MyMarker", "resultCode:" + resultCode);
        if (resultCode == -1) {
           this.avoid_codes = data.getBundleExtra("avoid_codes");

            EroomAsyncTask asyncTask = new EroomAsyncTask();
            asyncTask.execute();
        }
    }
}//EroomlistActivity.class
