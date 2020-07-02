
package com.kosmo.mukja.content;


import android.content.Context;

import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.SNSAdapter;
import com.kosmo.mukja.SNSItem;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent4 extends Fragment {
    public static Context context;
    private ListView listView_sns;


    private static final String TAG = "TestActivity";
    private ImageButton btn_sns;
    private EditText edit_sns;
    //2]onCreateView()오버 라이딩
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tabmenu4_layout, container, false);
        context = getContext();
        btn_sns = view.findViewById(R.id.btn_sns);
        edit_sns = view.findViewById(R.id.edit_sns);

        ImageView snsProfile= view.findViewById(R.id.exProfile);
        LinearLayout previewSNS = view.findViewById(R.id.previewSNS);


        snsProfile.setBackground(new ShapeDrawable(new OvalShape()));
        if(Build.VERSION.SDK_INT >= 21) {
            snsProfile.setClipToOutline(true);
        }

        btn_sns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("MyMarker","버튼클릭!");
                Log.i("MyMarker","입력된 내용:"+edit_sns.getText().toString());
                SearchSNS searchSNS=  new SearchSNS();
                searchSNS.execute("http://192.168.0.6:9876/flask?searchVal="+edit_sns.getText().toString());
                previewSNS.setVisibility(View.GONE);
            }
        });
        // 웹 서버로 데이터 전송


        listView_sns = view.findViewById(R.id.snslistView);


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
            JsonParser jsonParser = new JsonParser();
            JsonObject sns = (JsonObject) jsonParser.parse(result);
            Log.i("MyMarker","result json"+sns.toString());

            dataSetting(sns,sns.size());



            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask

    private void dataSetting(JsonObject sns,int snsLength){
        String snsProfile = null,  snsId = null,  snsContent = null, snsRe = null;

        Log.i("MyMarker","dataSetting json"+sns.toString());

        SNSAdapter adapter_SNS = new SNSAdapter();

        for (int i=0; i<snsLength; i++) {
            snsProfile=sns.getAsJsonObject(String.valueOf(i)).getAsJsonObject().get("profile").getAsString();

            snsId=sns.getAsJsonObject(String.valueOf(i)).getAsJsonObject().get("id").getAsString();
            snsContent=sns.getAsJsonObject(String.valueOf(i)).getAsJsonObject().get("img").getAsString();

            snsRe=sns.getAsJsonObject(String.valueOf(i)).getAsJsonObject().get("tags").getAsString().replaceAll("<div class=\\\"snsContent\\\">","\r\n\n").replaceAll("</div>"," ");
            Log.i("MyMarker","snsProfile"+snsProfile);
            Log.i("MyMarker","snsId"+snsId);
            Log.i("MyMarker","snsContent"+snsContent);
            adapter_SNS.addItem(snsProfile, snsId, snsContent,snsRe);
        }

        /* 리스트뷰에 어댑터 등록 */
        listView_sns.setAdapter(adapter_SNS);
    }
}