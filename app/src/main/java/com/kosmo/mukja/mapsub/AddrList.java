package com.kosmo.mukja.mapsub;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.ArrayList;
import java.util.List;

public class AddrList extends AppCompatActivity {

    private Context context;
    private Button close_btn2;
    //데이터를 저장하게 되는 리스트
    private List<String> list = new ArrayList<String>();
    private List<Double> lat_list = new ArrayList<Double>();
    private List<Double> lng_list = new ArrayList<Double>();
    private  ArrayAdapter<String> adapter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addrlist);
        Intent addrInent = getIntent();
        String search_text=addrInent.getStringExtra("search_text");
        Log.i("MyMarker","찾은 내용:"+search_text);
        context=AddrList.this;
        new SearchPlaceAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/Search/Place.do",search_text);

        ListView listview = (ListView)findViewById(R.id.listView);


        close_btn2=findViewById(R.id.close_btn2);
        close_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

       //리스트뷰와 리스트를 연결하기 위해 사용되는 어댑터
        adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, list);

        //리스트뷰의 어댑터를 지정해준다.
        listview.setAdapter(adapter);


        //리스트뷰의 아이템을 클릭시 해당 아이템의 문자열을 가져오기 위한 처리
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(lat_list.size()!=0){
                    Log.i("MyMarker","list.size():"+list.size());
                    Double lat = lat_list.get(position);
                    Log.i("MyMarker","lat:"+lat);
                    Double lng = lng_list.get(position);
                    Log.i("MyMarker","lng:"+lng);
                    addrInent.putExtra("lat",lat);
                    addrInent.putExtra("lng",lng);
                    setResult(1,addrInent);
                }
                finish();


            }
        });




    }


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class SearchPlaceAsyncTask extends AsyncTask<String,Void,String> {

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
            builder.setMessage("주소 리스트를 검색중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?dong=%s",params[0],params[1]));
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
            if(result==null){
                list.add("검색 결과가 없습니다");
                return;
            }


            JsonParser jsonParser = new JsonParser();
            JsonArray place = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker","찾은 장소"+place.toString());

            for(int i=0;i<place.size();i++){
                String addr="";

                addr+=" "+place.get(i).getAsJsonObject().get("sido");


                addr+=" "+place.get(i).getAsJsonObject().get("si_goon_go");


                addr+=" "+place.get(i).getAsJsonObject().get("dong");


                addr+=" "+place.get(i).getAsJsonObject().get("dong_sub");


               lat_list.add(Double.parseDouble(place.get(i).getAsJsonObject().get("lat").toString().replaceAll("\"","")));
               lng_list.add(Double.parseDouble(place.get(i).getAsJsonObject().get("lng").toString().replaceAll("\"","")));
               list.add(addr.replace("\"","").replace("null",""));


            }
            for(String addr : list) Log.i("MyMarker","addr:"+addr+" lat:"+lat_list.get(0)+"lng:"+ lng_list.get(0));
            adapter.notifyDataSetChanged();

           //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask
}
