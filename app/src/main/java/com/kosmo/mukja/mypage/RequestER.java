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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestER extends AppCompatActivity {

    private Context context;
    private String user_id;
    private ImageView er_close;
    private ListView requestER_listView;
    private ERAdapter erAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.request_er);

        context = RequestER.this;
        er_close =  findViewById(R.id.er_close);
        requestER_listView =  findViewById(R.id.requestER_listView);
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");


        er_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new GetERListAsyncTask().execute("http://" + TabContent2.ipAddr + ":8080/mukja/Andorid/Store/ER_list.do", user_id);

    }//oncreate



    private class GetERListAsyncTask extends AsyncTask<String, Void, String> {
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
            JsonArray request_er = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker", "찜" + request_er.toString());
            Log.i("MyMarker", "찜 size:" + request_er.size());

            erAdapter = new ERAdapter();

            for (int i = 0; i < request_er.size(); i++) {
                String store_name="",  store_id="",  er_title="",  er_postdate="",  user_id="",
                        erjoin_num="",  u_nick="",  u_tend="",  u_age="",  u_img="";

                if (request_er.get(i).getAsJsonObject().get("u_img") != null) {
                    u_img = "http://192.168.0.6:8080/mukja" + request_er.get(i).getAsJsonObject().get("u_img").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 u_img] : " + u_img);


                if (request_er.get(i).getAsJsonObject().get("store_name") != null) {
                    store_name = request_er.get(i).getAsJsonObject().get("store_name").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 store_name] : " + store_name);

                if (request_er.get(i).getAsJsonObject().get("store_id") != null) {
                    store_id = request_er.get(i).getAsJsonObject().get("store_id").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 store_id] : " + store_id);

                if (request_er.get(i).getAsJsonObject().get("er_title") != null) {
                    er_title = request_er.get(i).getAsJsonObject().get("er_title").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 er_title] : " + er_title);

                if (request_er.get(i).getAsJsonObject().get("er_postdate") != null) {
                    er_postdate = request_er.get(i).getAsJsonObject().get("er_postdate").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 er_postdate] : " + er_postdate);

                if (request_er.get(i).getAsJsonObject().get("user_id") != null) {
                    user_id = request_er.get(i).getAsJsonObject().get("user_id").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 user_id] : " + user_id);

                if (request_er.get(i).getAsJsonObject().get("erjoin_num") != null) {
                    erjoin_num = request_er.get(i).getAsJsonObject().get("erjoin_num").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 erjoin_num] : " + erjoin_num);


                if (request_er.get(i).getAsJsonObject().get("u_tend") != null) {
                    u_tend = request_er.get(i).getAsJsonObject().get("u_tend").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 u_tend] : " + u_tend);

                if (request_er.get(i).getAsJsonObject().get("u_nick") != null) {
                    u_nick = request_er.get(i).getAsJsonObject().get("u_nick").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 u_nick] : " + u_nick);

                if (request_er.get(i).getAsJsonObject().get("u_age") != null) {
                    u_age = request_er.get(i).getAsJsonObject().get("u_age").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[찜 u_age] : " + u_age);


                erAdapter.addItem(store_name, store_id, er_title, er_postdate, user_id,
                        erjoin_num, u_nick, u_tend, u_age, u_img);

            }
            requestER_listView.setAdapter(erAdapter);


            //다이얼로그 닫기
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask


}
