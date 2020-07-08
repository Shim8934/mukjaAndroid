package com.kosmo.mukja.review;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.kosmo.mukja.InsertReview;
import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StroeReview extends AppCompatActivity {

    private Context context;
    private ImageView storeClose;
    private ListView reviewListView;
    private ReviewAdapter reviewAdapter;
    private TextView insertReview;
    private String user_id;


    private void initView() {
        storeClose = (ImageView) findViewById(R.id.store_close);
        reviewListView = (ListView) findViewById(R.id.review_listView);
        insertReview = (TextView)findViewById(R.id.insertReview);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_review);
        initView();
        context=StroeReview.this;
        Intent intent = getIntent();
        String store_id = intent.getStringExtra("store_id");

        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");
        new ReviewAsyncTask().execute("http://"+ TabContent2.ipAddr +":8080/mukja/Andorid/Store/Review.do",store_id,user_id);
        storeClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        insertReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent insertReviewIntent = new Intent(context, InsertReview.class);
                startActivity(insertReviewIntent);
            }
        });
    }


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class ReviewAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("리뷰 불러오는 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            Log.i("MyMarker","요청주소:"+String.format("%s?username=%s&user_id=%s",params[0],params[1],params[2]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?username=%s&user_id=%s",params[0],params[1],params[2]));
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
            JsonArray review = (JsonArray) jsonParser.parse(result);
            Log.i("MyMarker","리뷰result"+review.toString());
            Log.i("MyMarker","리뷰result size:"+review.size());
            Log.i("MyMarker","리뷰user_id:"+user_id);
            reviewAdapter=new ReviewAdapter(user_id);

            for(int i=0; i<review.size(); i++){
                String reviewProfile = "";
                if(review.get(i).getAsJsonObject().get("u_img")!=null) {
                    reviewProfile= "http://192.168.0.6:8080/mukja"+review.get(i).getAsJsonObject().get("u_img").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[리뷰 reviewProfile] : "+reviewProfile);

                String reviewId = "";
                if(review.get(i).getAsJsonObject().get("u_nick")!=null) {
                    reviewId= review.get(i).getAsJsonObject().get("u_nick").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[리뷰 reviewId] : "+reviewId);

                String reviewIMG = "";
                if(review.get(i).getAsJsonObject().get("rf_path")!=null) {
                    reviewIMG= "http://192.168.0.6:8080/mukja"+review.get(i).getAsJsonObject().get("rf_path").toString().split(",")[0].replaceAll("\"","");
                }
                Log.i("MyMarker","[리뷰 reviewIMG] : "+reviewIMG);

                String reviewContent = "";
                if(review.get(i).getAsJsonObject().get("rv_content")!=null) {
                    reviewContent= review.get(i).getAsJsonObject().get("rv_content").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[리뷰 reviewContent] : "+reviewContent);

                Log.i("MyMarker","[리뷰 추출] : "+reviewProfile+" / "+reviewId+" / "+reviewIMG+" / "+reviewContent);
                String rv_no = review.get(i).getAsJsonObject().get("rv_no").toString().replaceAll("\"","");
                String id = review.get(i).getAsJsonObject().get("u_id").toString().replaceAll("\"","");

                reviewAdapter.addItem(reviewProfile,reviewId,reviewIMG,reviewContent,rv_no,id);

            }
            reviewListView.setAdapter(reviewAdapter);

            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask



}
