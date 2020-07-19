package com.kosmo.mukja.fcm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.appcompat.app.AppCompatActivity;
import com.gildaswise.horizontalcounter.HorizontalCounter;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreatERoomActivity extends AppCompatActivity {

    private HorizontalCounter horizontalCounter;
    private Context context;
    private TextView datePicker;
    private TextView timePicker;
    private Button close;
    private Button createRoom;
    private ToggleButton btnBD;
    private ToggleButton btnCW;
    private ToggleButton btnDP;
    private ToggleButton btnEG;
    private ToggleButton btnFL;
    private ToggleButton btnFS;
    private ToggleButton btnMK;
    private ToggleButton btnPE;
    private ToggleButton btnPK;
    private ToggleButton btnSB;
    private ToggleButton btnSF;
    private String store_id;
    private EditText title;
    private EditText content;
    private Bundle avoid_codes = new Bundle();
    private String titles;
    private String contents;
    private String horizontalCounters;
    private String datePickers;
    private String timePickers;
    private String tend="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createroom);
        Intent intent = getIntent();
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat ys = new SimpleDateFormat("yyyy", Locale.getDefault());
        SimpleDateFormat Ms = new SimpleDateFormat("MM", Locale.getDefault());
        SimpleDateFormat ds = new SimpleDateFormat("dd", Locale.getDefault());
        SimpleDateFormat Hs = new SimpleDateFormat("HH", Locale.getDefault());
        SimpleDateFormat ms = new SimpleDateFormat("mm", Locale.getDefault());

        int yyyy = Integer.parseInt(ys.format(currentTime));
        int MM = Integer.parseInt(Ms.format(currentTime));
        int dd = Integer.parseInt(ds.format(currentTime));
        int HH = Integer.parseInt(Hs.format(currentTime));
        int mm = Integer.parseInt(ms.format(currentTime));
        Log.i("가즈아", (HH + 9) + "시" + mm + "분");
        close = findViewById(R.id.close_btn);
        datePicker = findViewById(R.id.datepicker);
        timePicker = findViewById(R.id.timepicker);
        btnBD = findViewById(R.id.btnBD);
        btnCW = findViewById(R.id.btnCW);
        btnDP = findViewById(R.id.btnDP);
        btnEG = findViewById(R.id.btnEG);
        btnFL = findViewById(R.id.btnFL);
        btnFS = findViewById(R.id.btnFS);
        btnMK = findViewById(R.id.btnMK);
        btnPE = findViewById(R.id.btnPE);
        btnPK = findViewById(R.id.btnPK);
        btnSB = findViewById(R.id.btnSB);
        btnSF = findViewById(R.id.btnSF);
        title = findViewById(R.id.title);
        content = findViewById(R.id.content);
        createRoom = findViewById(R.id.createRoom_btn);
        horizontalCounter = findViewById(R.id.horizontalCounter);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });//close

        horizontalCounter.setOnReleaseListener(() -> {
            Toast.makeText(context, "Value updated to: " + horizontalCounter.getCurrentValue(), Toast.LENGTH_SHORT).show();
        });
        if (datePicker.getText() == "") {
            datePicker.setText(yyyy + "년" + MM + "월" + dd + "일");
        }
        if (timePicker.getText() == "") {
            timePicker.setText((HH + 9) + "시" + mm + "분");
        }
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(CreatERoomActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        datePicker.setText(year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일");
                    }
                }, yyyy, MM - 1, dd);
                dialog.show();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(CreatERoomActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePicker.setText(hourOfDay + "시" + minute + "분");
                    }
                }, (HH + 9), mm, true);
                dialog.show();
            }
        });

        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("BD")!=null){
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_b));
                    avoid_codes.remove("BD");
                    tend=tend.replaceAll("BD,","");
                }else{
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
                    avoid_codes.putString("BD","BD");
                    tend+="BD,";
                }
            }
        });
        btnCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("CW")!=null){
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_b));
                    avoid_codes.remove("CW");
                    tend=tend.replaceAll("CW,","");
                }else{
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
                    avoid_codes.putString("CW","CW");
                    tend+="CW,";

                }
            }
        });
        btnDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("DP")!=null){
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_b));
                    avoid_codes.remove("DP");
                    tend=tend.replaceAll("DP,","");
                }else{
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));
                    avoid_codes.putString("DP","DP");
                    tend+="DP,";

                }
            }
        });
        btnEG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("EG")!=null){
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_b));
                    avoid_codes.remove("EG");
                    tend=tend.replaceAll("EG,","");
                }else{
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
                    avoid_codes.putString("EG","EG");
                    tend+="EG,";
                }
            }
        });
        btnFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("FL")!=null){
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_b));
                    avoid_codes.remove("FL");
                    tend=tend.replaceAll("FL,","");
                }else{
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
                    avoid_codes.putString("FL","FL");
                    tend+="FL,";
                }
            }
        });
        btnFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("FS")!=null){
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_b));
                    avoid_codes.remove("FS");
                    tend= tend.replaceAll("FS,","");
                }else{
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
                    avoid_codes.putString("FS","FS");
                    tend+="FS,";
                }
            }
        });
        btnMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("MK")!=null){
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_b));
                    avoid_codes.remove("MK");
                    tend=tend.replaceAll("MK,","");
                }else{
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));
                    avoid_codes.putString("MK","MK");
                    tend+="MK,";
                }
            }
        });
        btnPE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("PE")!=null){
                    btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_b));
                    avoid_codes.remove("PE");
                    tend= tend.replaceAll("PE,","");
                }else{
                    btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_c));
                    avoid_codes.putString("PE","PE");
                    tend+="PE,";
                }
            }
        });
        btnPK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("PK")!=null){
                    btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_b));
                    avoid_codes.remove("PK");
                    tend=tend.replaceAll("PK,","");
                }else{
                    btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_c));
                    avoid_codes.putString("PK","PK");
                    tend+="PK,";
                }
            }
        });
        btnSB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("SB")!=null){
                    btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_b));
                    avoid_codes.remove("SB");
                    tend=tend.replaceAll("SB,","");
                }else{
                    btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_c));
                    avoid_codes.putString("SB","SB");
                    tend+="SB,";
                }
            }
        });
        btnSF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("SF")!=null){
                    btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_b));
                    avoid_codes.remove("SF");
                    tend= tend.replaceAll("SF,","");
                }else{
                    btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_c));
                    avoid_codes.putString("SF","SF");
                    tend+="SF,";
                }
            }
        });


        avoid_codes = intent.getBundleExtra("avoid_codes");
        createRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                store_id=intent.getStringExtra("store_id");
                titles = title.getText().toString();
               contents = content.getText().toString();
               horizontalCounters = horizontalCounter.getCurrentValue().toString().replaceAll(".0","");
                datePickers = datePicker.getText().toString();
                timePickers = timePicker.getText().toString();
                if(titles.isEmpty()||contents.isEmpty()){
                    if(titles.isEmpty()&&contents.isEmpty()){
                        title.setError("제목을 입력하세요");
                        content.setError("내용을 입력하세요");
                        return;
                    }
                    else if(titles.isEmpty()){
                        title.setError("제목을 입력하세요");
                        return;
                    }
                    else if(contents.isEmpty()){
                        content.setError("내용을 입력하세요");
                        return;
                    }
                }
                SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
                intent.putExtra("img", preferences.getString("img",null));
//                intent.putExtra("content", contents);
//                intent.putExtra("horizontalCounter", horizontalCounters);
//                intent.putExtra("datePicker", datePickers);
//                intent.putExtra("timePicker", timePickers);
                CreatEroomAsyncTask asyncTask = new CreatEroomAsyncTask();
                asyncTask.execute();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
 }//onCreat
    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class CreatEroomAsyncTask extends AsyncTask<String, Void, String> {

        private String userName;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
            userName = preferences.getString("username",null);
            Log.i("com.kosmo.mukja",userName);
        }

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();
            String u = "http://115.91.88.230:9998/mukja";
            String r = "/CreatEroom/json?";
            String l = String.format("username=%s&store_id=%s&title=%s&content=%s&max=%s&date=%s&time=%s&tend=%s"
                    ,userName,store_id,titles,contents,horizontalCounters,datePickers,timePickers,tend);
            try {
                Log.i("가즈아",u+r+l);
                URL url = new URL(u+r+l);
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

        }
    }///////////////AsyncTask

}
