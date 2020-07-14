package com.kosmo.mukja.mapsub;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class RealTimeTableInfo_Activity  extends AppCompatActivity {
    private Context context;
    private LinearLayout linear_table_info;
    private View element_view;
    private List row_list = new ArrayList();
    private List col_list = new ArrayList();
    private int rowcount;
    private int colcount;
    private int wating_count;
    private int avg_wating_time;
    private Timer t;
    private TimerTask timerTask;
    private LinearLayout wating;
    private TextView tv_watingTime;
    private  TextView tv_watingCount ;
    private int breaker=0;
    @SuppressLint("ResourceType")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent=getIntent();
        String store_id= intent.getStringExtra("store_id").replaceAll("\"","");
        Log.i("MyMarker","store_id:"+store_id);

        context=RealTimeTableInfo_Activity.this;

        new RealtimeInitAsyncTask().execute("http://"+ TabContent2.ipAddr +"/mukja/getRealTimeReservation.do",store_id);

        new RealtimeUpdateAsyncTask().execute("http://"+ TabContent2.ipAddr +"/mukja/getRealTimeReservation.do",store_id);
    }

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class RealtimeUpdateAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... params) {
            t=new Timer();
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if(breaker!=0){
                        StringBuffer buf = new StringBuffer();
                        try {
                            URL url = new URL(String.format("%s?store_id=%s",params[0],params[1]));
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
                        }catch(Exception e){
                            e.printStackTrace();
                        }

                        String result = buf.toString();
                        JsonParser jsonParser = new JsonParser();
                        JsonObject tableinfo = (JsonObject) jsonParser.parse(result);
                        colcount = Integer.parseInt(tableinfo.get("x_boundary").toString().trim().replaceAll("\"",""));
                        rowcount = Integer.parseInt(tableinfo.get("y_boundary").toString().trim().replaceAll("\"",""));
                        wating_count = Integer.parseInt(tableinfo.get("wating_count").toString().trim().replaceAll("\"",""));
                        Log.i("MyMarker","rowcount:"+rowcount +"colcount :"+colcount);

                        int f=0;
                        for(int i=1;i<=rowcount;i++) {
                            for (int j = 1; j <=colcount; j++) {
                                View ele;
                                if(f==0){
                                    ele = findViewById(82820+j);
                                }else{
                                    ele = findViewById(82820+j+(colcount*f));
                                }
                                if (tableinfo.get(String.valueOf(i-1)).getAsJsonArray().get(j-1).toString().replaceAll("\"", "").equals("w")) {
                                    ele.setBackgroundResource(R.drawable.realtime_view);
                                } else if (tableinfo.get(String.valueOf(i-1)).getAsJsonArray().get(j-1).toString().replaceAll("\"", "").equals("g")) {
                                    ele.setBackgroundResource(R.drawable.realtime_view_g);
                                } else {
                                    ele.setBackgroundResource(R.drawable.realtime_view_b);
                                }//if else

                            }//jfor
                            f++;
                        }//ifor
                        runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                tv_watingTime.setText("대기 시간 : "+avg_wating_time*wating_count+"분");
                                tv_watingCount.setText("대기 팀 :"+wating_count+"팀");
                            }
                        });
                    }//breaker
                }//timerRun
            };//timerTesk
            t.schedule(timerTask,0,300);
            return "";
        }///////////doInBackground

        @Override
        protected void onPostExecute(String result) {
            Log.i("MyMarker","result:"+result);
        }//onPostExecute
    }///////////////AsyncTask

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class RealtimeInitAsyncTask extends AsyncTask<String,Void,String> {

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
            builder.setMessage("실시간 좌석현황 로드 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute


        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?store_id=%s",params[0],params[1]));
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
            Log.i("MyMarker","result:"+result);


            JsonParser jsonParser = new JsonParser();
            JsonObject tableinfo = (JsonObject) jsonParser.parse(result);
            colcount = Integer.parseInt(tableinfo.get("x_boundary").toString().trim().replaceAll("\"",""));
            rowcount = Integer.parseInt(tableinfo.get("y_boundary").toString().trim().replaceAll("\"",""));
            wating_count = Integer.parseInt(tableinfo.get("wating_count").toString().trim().replaceAll("\"",""));
            avg_wating_time = Integer.parseInt(tableinfo.get("avg_wating_time").toString().trim().replaceAll("\"",""));
            Log.i("MyMarker","rowcount:"+rowcount +"colcount :"+colcount);
            Log.i("MyMarker","  tableinfo.get(String.valueOf(0)):"+  tableinfo.get(String.valueOf(0)));

            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            LinearLayout linearLayout= ( LinearLayout) inflater.inflate(R.layout.activity_realtime,null);
            linearLayout.setGravity(Gravity.CENTER);

            linear_table_info=new LinearLayout(context);
            linear_table_info.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
            linear_table_info.setOrientation(LinearLayout.VERTICAL);
            int idcount=82821;
            for(int i=1;i<=rowcount;i++) {
                LinearLayout row =new LinearLayout(context);
                row.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
                row.setGravity(Gravity.CENTER);
                for(int j=1;j<=colcount;j++) {
                    View ele = new View(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(60, 60);
                    layoutParams.setMargins(3, 3, 3, 3);
                    ele.setLayoutParams(layoutParams);
                    ele.setId(idcount);
                    Log.i("MyMarker","  ele ID:"+ ele.getId());
                    idcount+=1;
                    row.addView(ele);
                    col_list.add(ele);
                    ele.setBackgroundResource(R.drawable.realtime_view);
                    if( tableinfo.get(String.valueOf(i-1)).getAsJsonArray().get(j-1).toString().replaceAll("\"","").equals("w")){
                        ele.setBackgroundResource(R.drawable.realtime_view);
                    }else if(tableinfo.get(String.valueOf(i-1)).getAsJsonArray().get(j-1).toString().replaceAll("\"","").equals("g")){
                        ele.setBackgroundResource(R.drawable.realtime_view_g);
                    }else{
                        ele.setBackgroundResource(R.drawable.realtime_view_b);
                    }

                }
                row_list.add(col_list);
                linear_table_info.addView(row);
                linear_table_info.setGravity(Gravity.CENTER);

            }

            wating =new LinearLayout(context);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.gravity = Gravity.CENTER;

            wating.setOrientation(LinearLayout.VERTICAL);
            wating.setLayoutParams(layoutParams);
            tv_watingTime = new TextView(context);
            tv_watingTime.setTextSize(22);
            tv_watingTime.setGravity(Gravity.CENTER);

            tv_watingCount = new TextView(context);
            tv_watingCount.setTextSize(22);
            tv_watingCount.setGravity(Gravity.CENTER);

            tv_watingTime.setText("대기 시간 : "+avg_wating_time*wating_count+"분");
            tv_watingCount.setText("대기 팀 :"+wating_count+"팀");
            wating.addView(tv_watingCount);
            wating.addView(tv_watingTime);


            linearLayout.addView(linear_table_info);
            linearLayout.addView(wating);

            setContentView(linearLayout);

            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();

            //싱크맞추기 위한 브레이커
            breaker=1;
        }//onPostExecute
    }///////////////AsyncTask

    @Override
    protected void onDestroy() {
        super.onDestroy();
        t.cancel();
    }//onDestroy

}
