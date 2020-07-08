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

public class MyER extends AppCompatActivity {

    private Context context;
    private String user_id;
    private ImageView myer_close;
    private ListView mMyER_listView;
    private MyERAdapter myERAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_er);

        context = MyER.this;
        myer_close =  findViewById(R.id.myer_close);
        mMyER_listView =  findViewById(R.id.MyER_listView);
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");


        myer_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        new GetERListAsyncTask().execute("http://" + TabContent2.ipAddr + ":8080/mukja/Andorid/Store/MyER_list.do", user_id);

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
            JsonArray my_erlist = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker", "찜" + my_erlist.toString());
            Log.i("MyMarker", "찜 size:" + my_erlist.size());

            myERAdapter = new MyERAdapter();

            for (int i = 0; i < my_erlist.size(); i++) {
                 String er_no="";
                 String erjoin_num="";
                 String store_name="";
                 String er_title="";
                 String er_tend="";
                 String er_max="";
                 String er_time="";
                 String u_nick="";
                 String u_tend="";
                 String u_age="";
                 String u_img="";
                 String u_id="";
                if (my_erlist.get(i).getAsJsonObject().get("u_img") != null) {
                    u_img = "http://192.168.0.6:8080/mukja" + my_erlist.get(i).getAsJsonObject().get("u_img").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er u_img] : " + u_img);


                if (my_erlist.get(i).getAsJsonObject().get("er_no") != null) {
                    er_no = my_erlist.get(i).getAsJsonObject().get("er_no").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er er_no] : " + er_no);

                if (my_erlist.get(i).getAsJsonObject().get("erjoin_num") != null) {
                    erjoin_num = my_erlist.get(i).getAsJsonObject().get("erjoin_num").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er erjoin_num] : " + erjoin_num);

                if (my_erlist.get(i).getAsJsonObject().get("store_name") != null) {
                    store_name = my_erlist.get(i).getAsJsonObject().get("store_name").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er store_name] : " + store_name);

                if (my_erlist.get(i).getAsJsonObject().get("er_title") != null) {
                    er_title = my_erlist.get(i).getAsJsonObject().get("er_title").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er er_title] : " + er_title);

                if (my_erlist.get(i).getAsJsonObject().get("er_tend") != null) {
                    er_tend = my_erlist.get(i).getAsJsonObject().get("er_tend").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er er_tend] : " + er_tend);

                if (my_erlist.get(i).getAsJsonObject().get("er_max") != null) {
                    er_max = my_erlist.get(i).getAsJsonObject().get("er_max").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er er_max] : " + er_max);

                if (my_erlist.get(i).getAsJsonObject().get("er_time") != null) {
                    er_time = my_erlist.get(i).getAsJsonObject().get("er_time").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er er_time] : " + er_time);

                if (my_erlist.get(i).getAsJsonObject().get("u_nick") != null) {
                    u_nick = my_erlist.get(i).getAsJsonObject().get("u_nick").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er u_nick] : " + u_nick);

                if (my_erlist.get(i).getAsJsonObject().get("u_tend") != null) {
                    u_tend = my_erlist.get(i).getAsJsonObject().get("u_tend").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er u_tend] : " + u_tend);

                if (my_erlist.get(i).getAsJsonObject().get("u_age") != null) {
                    u_age = my_erlist.get(i).getAsJsonObject().get("u_age").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er u_age] : " + u_age);

                if (my_erlist.get(i).getAsJsonObject().get("er_currcount") != null) {
                    er_max += "명/"+my_erlist.get(i).getAsJsonObject().get("er_currcount").toString().replaceAll("\"", "")+"명";
                }
                Log.i("MyMarker", "[참여한er er_currcount] : " + er_max);

                if (my_erlist.get(i).getAsJsonObject().get("u_id") != null) {
                    u_id = my_erlist.get(i).getAsJsonObject().get("u_id").toString().replaceAll("\"", "");
                }
                Log.i("MyMarker", "[참여한er u_id] : " + u_id);

                myERAdapter.addItem(   er_no,erjoin_num,store_name,er_title,
                        er_tend,er_max,er_time,u_nick,u_tend,u_age, u_img,u_id);
            }
            mMyER_listView.setAdapter(myERAdapter);


            //다이얼로그 닫기
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask


}
