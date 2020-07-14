package com.kosmo.mukja.mypage;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.content.TabContent2;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class EditMyInfo extends AppCompatActivity {

    private Context context;
    private String user_id;
    private ImageView myInfoClose;
    private TextView myinfoId;
    private EditText myinfoPwd;
    private EditText myinfoPwd2;
    private EditText myinfoNick;
    private EditText myinfoPh;
    private EditText myinfo_addr;
    private RadioGroup myinfoAgegroup;
    private ToggleButton btnBD;
    private ToggleButton btnCW;
    private ToggleButton btnDP;
    private ToggleButton btnEG;
    private ToggleButton btnFL;
    private ToggleButton btnFS;
    private ToggleButton btnMK;
    private ToggleButton btnPE;
    private ToggleButton btnSF;
    private ToggleButton btnPK;
    private ToggleButton btnSB;
    private Bundle avoid_codes=new Bundle();
    private RadioButton age_10,age_20,age_30,age_40,age_50,age_60;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myinfo);
        context=EditMyInfo.this;
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", Context.MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");
        initView();


        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnBD.isChecked()){
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_b));
                }else{
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
                }
            }
        });
        btnCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnCW.isChecked()){
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_b));
                }else{
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
                }
            }
        });
        btnDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnDP.isChecked()){
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_b));
                }else{
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));

                }
            }
        });
        btnEG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnEG.isChecked()){
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_b));
                }else{
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
                }
            }
        });
        btnFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFL.isChecked()){
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_b));
                }else{
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
                }
            }
        });
        btnFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnFS.isChecked()){
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_b));
                }else{
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
                }
            }
        });
        btnMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnMK.isChecked()){
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_b));
                }else{
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));

                }
            }
        });
        btnPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("PE")!=null){
                    btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_b));
                    avoid_codes.remove("PE");
                }else{
                    btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_c));
                    avoid_codes.putString("PE","PE");
                }
            }
        });
        btnPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("PK")!=null){
                    btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_b));
                    avoid_codes.remove("PK");
                }else{
                    btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_c));
                    avoid_codes.putString("PK","PK");
                }
            }
        });
        btnSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("SB")!=null){
                    btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_b));
                    avoid_codes.remove("SB");
                }else{
                    btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_c));
                    avoid_codes.putString("SB","SB");
                }
            }
        });
        btnSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("SF")!=null){
                    btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_b));
                    avoid_codes.remove("SF");
                }else{
                    btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_c));
                    avoid_codes.putString("SF","SF");
                }
            }
        });






        new GetMyInfoAsyncTask().execute("http://" + TabContent2.ipAddr + "/mukja/Andorid/User/UserInfo.do", user_id);


    }//oncreate

    private void initView() {
        myInfoClose = (ImageView) findViewById(R.id.myInfo_close);
        myinfoId = (TextView) findViewById(R.id.myinfo_id);

        myinfoPwd = (EditText) findViewById(R.id.myinfo_pwd);
        myinfoPwd2 = (EditText) findViewById(R.id.myinfo_pwd2);
        myinfoNick = (EditText) findViewById(R.id.myinfo_nick);
        myinfoPh = (EditText) findViewById(R.id.myinfo_ph);
        myinfo_addr =(EditText) findViewById(R.id.myinfo_addr);

        myinfoAgegroup = (RadioGroup) findViewById(R.id.myinfo_agegroup);

        btnBD = (ToggleButton) findViewById(R.id.btnBD);
        btnCW = (ToggleButton) findViewById(R.id.btnCW);
        btnDP = (ToggleButton) findViewById(R.id.btnDP);
        btnEG = (ToggleButton) findViewById(R.id.btnEG);
        btnFL = (ToggleButton) findViewById(R.id.btnFL);
        btnFS = (ToggleButton) findViewById(R.id.btnFS);
        btnMK = (ToggleButton) findViewById(R.id.btnMK);
        btnPE = (ToggleButton) findViewById(R.id.btnPE);
        btnSF = (ToggleButton) findViewById(R.id.btnSF);
        btnPK = (ToggleButton) findViewById(R.id.btnPK);
        btnSB = (ToggleButton) findViewById(R.id.btnSB);

        age_10=findViewById(R.id.age_10);
        age_20=findViewById(R.id.age_20);
        age_30=findViewById(R.id.age_30);
        age_40=findViewById(R.id.age_40);
        age_50=findViewById(R.id.age_50);
        age_60=findViewById(R.id.age_60);
    }



    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class GetMyInfoAsyncTask extends AsyncTask<String,Void,String> {
        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setIcon(android.R.drawable.btn_star_big_on);
            builder.setMessage("불러오는 중");

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
            JsonObject userinfo = (JsonObject) jsonParser.parse(result);
            Log.i("MyMarker","Userinfo:"+userinfo.toString());
            myinfoId.setText(userinfo.get("u_id").toString().replaceAll("\"",""));
            myinfoPwd.setText((userinfo.get("u_pwd").toString().replaceAll("\"","")));
            myinfoNick.setText(userinfo.get("u_nick").toString().replaceAll("\"",""));
            myinfo_addr.setText(userinfo.get("u_addr").toString().replaceAll("\"",""));
            myinfoPh.setText(userinfo.get("u_ph").toString().replaceAll("\"",""));
            Log.i("MyMarker","age:"+Integer.parseInt(userinfo.get("u_age").toString().replaceAll("\"",""))/10);
            int age = (Integer.parseInt(userinfo.get("u_age").toString().replaceAll("\"","")))/10 ;
            switch (age){
                case 1:
                    age_10.setChecked(true);
                    break;
                case 2:
                    age_20.setChecked(true);
                    break;
                case 3:
                    age_30.setChecked(true);
                    break;
                case 4:
                    age_40.setChecked(true);
                    break;
                case 5:
                    age_50.setChecked(true);
                    break;
                default:
                    age_60.setChecked(true);
            }



            List<String> array_codes = new Vector<String>(Arrays.asList(userinfo.get("u_oldTend").toString().replaceAll("\"","").split(",")));

            for(int i=0; i<array_codes.size();i++) {
                avoid_codes.putString(array_codes.get(i),array_codes.get(i));
            }
            Log.i("MyMarker","avoid_codes.toString():"+avoid_codes.toString());

            for (String code:array_codes){
                Log.i("MyMarker","탠드개별코드:"+code);
                if(code.equals("BD")) {
                    btnBD.setChecked(true);
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
                }

                if(code.equals("CW")) {
                    btnCW.setChecked(true);
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
                }

                if(code.equals("DP")) {
                    btnDP.setChecked(true);
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));
                }

                if(code.equals("EG")) {
                    btnEG.setChecked(true);
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
                }

                if(code.equals("FL")) {
                    btnFL.setChecked(true);
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
                }

                if(code.equals("FS")) {
                    btnFS.setChecked(true);
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
                }

                if(code.equals("MK")) {
                    btnMK.setChecked(true);
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));
                }

                if(code.equals("PE")) {
                    btnPE.setChecked(true);
                    btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_c));
                }

                if(code.equals("PK")) {
                    btnPK.setChecked(true);
                    btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_c));
                }

                if(code.equals("SB")) {
                    btnSB.setChecked(true);
                    btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_c));
                }

                if(code.equals("SF")) {
                    btnSF.setChecked(true);
                    btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_c));
                }

            }


            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();


        }
    }///////////////AsyncTask


}
