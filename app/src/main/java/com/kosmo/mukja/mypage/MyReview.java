package com.kosmo.mukja.mypage;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.content.TabContent3;
import com.kosmo.mukja.review.ReviewAdapter;
import com.kosmo.mukja.review.ReviewItem;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MyReview extends AppCompatActivity {
    private ImageView myReview_close;
    private ListView myreviewListView;
    private ReviewAdapter reviewAdapter;
    private Context context;
    private  String username;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myreview);
        initView();
        context=MyReview.this;

        SharedPreferences preferences = context.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        username = preferences.getString("username", "defaultID");


        myReview_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        new MyReviewAsyncTask().execute("http://"+ TabContent2.ipAddr +"/mukja/Andorid/Store/MyReview.do",username);
    }//oncreate

    private class MyReviewAsyncTask extends AsyncTask<String,Void,String> {
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
            Log.i("MyMarker","요청주소:"+String.format("%s?username=%s",params[0],params[1]));
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("%s?username=%s",params[0],params[1]));
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
            Log.i("MyMarker","마이리뷰result"+review.toString());
            Log.i("MyMarker","마이리뷰result size:"+review.size());
            reviewAdapter=new ReviewAdapter(username);
            for(int i=0; i<review.size(); i++){
                String reviewProfile = "";
                if(review.get(i).getAsJsonObject().get("u_img")!=null) {
                    reviewProfile= "http://"+TabContent2.ipAddr+"/mukja"+review.get(i).getAsJsonObject().get("u_img").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[마이리뷰 reviewProfile] : "+reviewProfile);

                String reviewId = "";
                if(review.get(i).getAsJsonObject().get("u_nick")!=null) {
                    reviewId= review.get(i).getAsJsonObject().get("u_nick").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[마이리뷰 reviewId] : "+reviewId);

                String reviewIMG = "";
                if(review.get(i).getAsJsonObject().get("rf_path")!=null) {
                    reviewIMG= "http://"+TabContent2.ipAddr+"/mukja"+review.get(i).getAsJsonObject().get("rf_path").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[마이리뷰 reviewIMG] : "+reviewIMG);

                String reviewContent = "";
                if(review.get(i).getAsJsonObject().get("rv_content")!=null) {
                    reviewContent= review.get(i).getAsJsonObject().get("rv_content").toString().replaceAll("\"","");
                }
                Log.i("MyMarker","[마이리뷰 reviewContent] : "+reviewContent);

                Log.i("MyMarker","[마이리뷰 추출] : "+reviewProfile+" / "+reviewId+" / "+reviewIMG+" / "+reviewContent);
                String rv_no = review.get(i).getAsJsonObject().get("rv_no").toString().replaceAll("\"","");
                String id = review.get(i).getAsJsonObject().get("u_id").toString().replaceAll("\"","");
                reviewAdapter.addItem(reviewProfile,reviewId,reviewIMG,reviewContent,rv_no,id);

            }
            myreviewListView.setAdapter(reviewAdapter);




            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask



    private void initView() {
        myReview_close =  findViewById(R.id.myReview_close);
        myreviewListView = findViewById(R.id.myreview_listView);
    }
}
