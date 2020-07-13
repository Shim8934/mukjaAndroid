package com.kosmo.mukja.content;

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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.mypage.EditMyInfo;
import com.kosmo.mukja.mypage.MyER;
import com.kosmo.mukja.mypage.MyFallow;
import com.kosmo.mukja.mypage.MyReview;
import com.kosmo.mukja.mypage.RequestER;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent3 extends Fragment {
    private ImageView userProfile;
    private TextView nickname;
    private TextView tend;
    private TextView addr;
    private Button btnEditProfile;
    private LinearLayout myreview;
    private LinearLayout likestore;
    private LinearLayout requestER;
    private LinearLayout myER;
    private View view;
    private Context context;


    //2]onCreateView()오버 라이딩
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tabmenu3_layout, container, false);
        initView();
        context=getContext();

        SharedPreferences preferences = context.getSharedPreferences("loginInfo",Context.MODE_PRIVATE);
        String username = preferences.getString("username", "defaultID");
        new UserInfoAsyncTask().execute("http://"+ TabContent2.ipAddr +":9998/mukja/Andorid/User/UserInfo.do",username);

        myreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyReview.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        likestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyFallow.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        requestER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, RequestER.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        myER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MyER.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, EditMyInfo.class);
                intent.putExtra("username",username);
                startActivity(intent);
            }
        });
        return view;
    }






    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class UserInfoAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;


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
            JsonObject userinfo = (JsonObject) jsonParser.parse(result);
            Log.i("MyMarker","Userinfo:"+userinfo.toString());
            nickname.setText(userinfo.get("u_nick").toString().replaceAll("\"",""));
            tend.setText(userinfo.get("u_tend").toString().replaceAll("\"",""));
            addr.setText(userinfo.get("u_addr").toString().replaceAll("\"",""));

            String imgPath = userinfo.get("u_img").toString().replaceAll("\"","");
            userProfile.setBackground(new ShapeDrawable(new OvalShape()));
            if(Build.VERSION.SDK_INT >= 21) {
                userProfile.setClipToOutline(true);
            }

            Picasso.get().load("http://"+TabContent2.ipAddr+":9998/mukja"+imgPath).into(userProfile);


        }
    }///////////////AsyncTask
    private void initView() {
        userProfile = view. findViewById(R.id.user_profile);
        nickname = view. findViewById(R.id.nickname);
        tend = view. findViewById(R.id.tend);
        addr = view. findViewById(R.id.addr);
        btnEditProfile = view. findViewById(R.id.btn_editProfile);
        myreview = view. findViewById(R.id.myreview);
        likestore = view. findViewById(R.id.likestore);
        requestER =  view.findViewById(R.id.requestER);
        myER =view. findViewById(R.id.myER);
    }//initView

}
