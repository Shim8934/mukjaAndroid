package com.kosmo.mukja.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.review.ReviewAdapter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyFallow extends AppCompatActivity {

    private Context context;
    private String user_id;
    private ImageView myFallowClose;
    private ListView myfallowListView;
    private FallowAdapter fallowAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myfallow);

        context = MyFallow.this;
        myFallowClose =  findViewById(R.id.myfallow_close);
        myfallowListView =  findViewById(R.id.myfallow_listView);
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");


        myFallowClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new FallowListAsyncTask().execute("http://" + TabContent2.ipAddr + "/mukja/Andorid/Store/FallowList.do", user_id);

    }//oncreate



    private class FallowListAsyncTask extends AsyncTask<String, Void, String> {
        private AlertDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("불러오는 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker", "요청주소:" + String.format("%s?user_id=%s", params[0], params[1]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?user_id=%s", params[0], params[1]));
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

            JsonParser jsonParser = new JsonParser();
            JsonArray mystore = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker", "찜" + mystore.toString());
            Log.i("MyMarker", "찜 size:" + mystore.size());
            fallowAdapter = new FallowAdapter();
            for (int i = 0; i < mystore.size(); i++) {
                String ms_img = "";
                if (mystore.get(i).getAsJsonObject().get("ms_img") != null) {
                    ms_img = "http://"+TabContent2.ipAddr+"/mukja" + mystore.get(i).getAsJsonObject().get("ms_img").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 ms_img] : " + ms_img);

                String ms_no = "";
                if (mystore.get(i).getAsJsonObject().get("ms_no") != null) {
                    ms_no = mystore.get(i).getAsJsonObject().get("ms_no").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 ms_no] : " + ms_no);

                String ms_storename = "";
                if (mystore.get(i).getAsJsonObject().get("ms_storename") != null) {
                    ms_storename = mystore.get(i).getAsJsonObject().get("ms_storename").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 ms_storename] : " + ms_storename);

                String ms_postdate = "";
                if (mystore.get(i).getAsJsonObject().get("ms_postdate") != null) {
                    ms_postdate = mystore.get(i).getAsJsonObject().get("ms_postdate").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 ms_postdate] : " + ms_postdate);

                String ms_storeid = "";
                if (mystore.get(i).getAsJsonObject().get("ms_storeid") != null) {
                    ms_storeid = mystore.get(i).getAsJsonObject().get("ms_storeid").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 ms_storeid] : " + ms_storeid);


                fallowAdapter.addItem(ms_no, ms_storename, ms_postdate, ms_img, ms_storeid);

            }
            myfallowListView.setAdapter(fallowAdapter);


            //다이얼로그 닫기
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask


}
