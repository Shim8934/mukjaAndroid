package com.kosmo.mukja;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.fcm.ERDTO;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.overlay.OverlayImage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.eroomlist_layout);
        ButterKnife.bind(this);
        EroomAsyncTask asyncTask = new EroomAsyncTask();
        asyncTask.execute();
        //tabContent2 fragment에서 eroom_list(모임리스트) 버튼 클릭시 intent를 넘긴걸 받아온다.
        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id").replaceAll("\"", "");
        Log.i("이거다", store_id);


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
                    erdto.setU_img(eroomInfo.get("u_img").toString().replaceAll("\"", ""));
                    erdto.setEr_title(eroomInfo.get("er_title").toString().replaceAll("\"", ""));
                    erdto.setEr_content(eroomInfo.get("er_content").toString().replaceAll("\"", ""));
                    arrayList.add(erdto);
                }
                showChatList();
            }



        }

    }///////////////AsyncTask
    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        eroomAdapter = new EroomAdapter(getApplicationContext(), arrayList);
        room_list.setAdapter(eroomAdapter);
    }
}//EroomlistActivity.class
