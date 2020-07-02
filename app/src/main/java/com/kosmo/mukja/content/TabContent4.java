
package com.kosmo.mukja.content;


import android.content.Context;

import android.os.AsyncTask;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent4 extends Fragment {
    private Context context;


    private static final String TAG = "TestActivity";
    private Button btn_sns;
    private EditText edit_sns;
    //2]onCreateView()오버 라이딩
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabmenu4_layout, container, false);
        context = getContext();
        btn_sns = view.findViewById(R.id.btn_sns);
        edit_sns = view.findViewById(R.id.edit_sns);

        btn_sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyMarker","버튼클릭!");
                Log.i("MyMarker","입력된 내용:"+edit_sns.getText().toString());
                SearchSNS searchSNS=  new SearchSNS();
                searchSNS.execute("http://f2cd15e7bc7b.ngrok.io/flask?searchVal="+edit_sns.getText().toString());
            }
        });
        // 웹 서버로 데이터 전송
        return view;

    }
    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class SearchSNS extends AsyncTask<String,Void,String> {

        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("SNS에서 검색어에 대한 내용을 검색중입니다.");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();
            Log.i("MyMarker","주소 :"+String.format("%s",params[0]));



            try {
                URL url = new URL(String.format("%s",params[0]));
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
            JsonObject sns = (JsonObject) jsonParser.parse(result);
            Log.i("MyMarker","찾은 장소"+sns.toString());
            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask


}