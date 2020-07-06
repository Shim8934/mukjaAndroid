package com.kosmo.mukja.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.kosmo.mukja.LoginActivity;
import com.kosmo.mukja.MainActivity;
import com.kosmo.mukja.R;
import com.kosmo.mukja.SampleAdapter;
import com.naver.maps.map.a.c;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import pyxis.uzuki.live.rollingbanner.RollingBanner;
import pyxis.uzuki.live.rollingbanner.RollingViewPagerAdapter;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent5 extends Fragment {
    //2]onCreateView()오버 라이딩
    private RollingBanner rollingBanner;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_remain, container, false);
//        MainAsyncTask mainAsyncTask = new MainAsyncTask();
//        mainAsyncTask.execute();
        rollingBanner = view.findViewById(R.id.banner);
        Log.i("dddd", "여기까지옴");
        SampleAdapter adapter = new SampleAdapter(context, new ArrayList<>(Arrays.asList()));
        Log.i("dddd", "여기까지옴");
        rollingBanner.setAdapter(adapter);

        return view;
    }


    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class MainAsyncTask extends AsyncTask<String, Void, String> {
        private AlertDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setIcon(android.R.drawable.ic_menu_compass);
            builder.setTitle("로그인");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();
        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();
            try {
                //URL url = new URL(String.format("%s?username=%s&password=%s",params[0],params[1],params[2]));
                URL url = new URL("http://192.168.0.12:8080/mukja/mainAndroid.bbs");
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

            //서버로부터 받은 데이타(JSON형식) 파싱
            //회원이 아닌 경우 빈 문자열
            Log.i("com.kosmo.mukja", "result:" + result);
//            if(result !=null && result.length()!=0) {//회원인 경우
//                try {
//                    JSONObject json = new JSONObject(result);
//
//                    String username = json.getString("username");
//                    Log.i("com.kosmo.mukja","username:"+username);

//                    Users member = new Users();
//
//
//                    mDatabase.child("users").child("member").setValue(member)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//                                    // Write was successful!
//                                    Toast.makeText(LoginActivity.this, "저장을 완료했습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            })
//                            .addOnFailureListener(new OnFailureListener() {
//                                @Override
//                                public void onFailure(@NonNull Exception e) {
//                                    // Write failed
//                                    Toast.makeText(LoginActivity.this, "저장을 실패했습니다.", Toast.LENGTH_SHORT).show();
//                                }
//                            });
//
//                    Intent intent = new Intent(context,MainActivity.class);
//                    intent.putExtra("username",username);
//                    startActivity(intent);
//                    //finish()불필요-NO_HISTORY로 설정했기때문에(매니페스트에서)
//                    //아이디 비번저장
//                    SharedPreferences preferences = context.getSharedPreferences("loginInfo",context.MODE_PRIVATE);
//                    SharedPreferences.Editor editor =preferences.edit();
//                    editor.putString("username",json.getString("username"));
//                    editor.putString("password",json.getString("password"));
//                    editor.commit();
//                }
//                catch(Exception e){e.printStackTrace();}
//
//            }
//            else{//회원이 아닌 경우
//                Toast.makeText(context,"아이디와 비번이 일치하지 않아요",Toast.LENGTH_SHORT).show();
//            }
//
//            //다이얼로그 닫기
//            if(progressDialog!=null && progressDialog.isShowing())
//                progressDialog.dismiss();
//        }

        }///////////////LoginAsyncTask


    }
}
