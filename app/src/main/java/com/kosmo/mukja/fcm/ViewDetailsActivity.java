package com.kosmo.mukja.fcm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class ViewDetailsActivity extends AppCompatActivity {

    private TextView filterText;
    private Button closeBtn;
    private ImageView Profile;
    private TextView titles;
    private TextView contents;
    private TextView nickname;
    private TextView ages;
    private TextView maxs;
    private TextView date;
    private TextView time;
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
    private Button JoinRoomBtn;
    private String userName;
    private Context context;
    private int er_no;
    private String store_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_details);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 1); //Display 사이즈의 70%
        int height = (int) (display.getHeight() * 1);  //Display 사이즈의 90%
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        Intent intent = getIntent();
        initView();
        er_no=intent.getIntExtra("er_no",0);
        store_id=intent.getStringExtra("store_id");
        Log.i("가즈아",store_id);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });
        JoinRoomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
                userName = preferences.getString("username",null);
                Log.i("가즈아",userName);
                String master =intent.getStringExtra("master");
                Log.i("가즈아",master);

                if(userName.equals(master)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                    builder.setMessage("내가 만든 채팅방입니다.");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                    builder.show();
                }
                else{
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                    builder.setMessage("참여하시겠습니까?");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                ViewDetailsActivity.Details asyncTask = new ViewDetailsActivity.Details();
                                asyncTask.execute();
                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"아니오를 선택했습니다.",Toast.LENGTH_LONG).show();
                                }
                            });
                    builder.show();

                }

            }
        });




        String content = intent.getStringExtra("content").replace("<p>", "").replace("</p>", "");
        String dateAll = intent.getStringExtra("time");
        String[] dateArray = dateAll.split("일");
        String dates = dateArray[0] + "일";
        String times = dateArray[1].trim();

        Picasso.get().load(Uri.parse("http://115.91.88.230:9998/mukja" + intent.getStringExtra("img"))).resize(200, 100).into(Profile);
        titles.setText(intent.getStringExtra("title"));
        contents.setText(content);
        nickname.setText(intent.getStringExtra("nick"));
        ages.setText(intent.getStringExtra("age"));
        maxs.setText(intent.getStringExtra("max"));
        date.setText(dates);
        time.setText(times);
        String tens = intent.getStringExtra("tend");
        Log.i("가즈아",tens);

        if(tens.indexOf("BD")!=-1) {
            btnBD.setChecked(true);
            btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
        }

        if(tens.indexOf("CW")!=-1) {
            btnCW.setChecked(true);
            btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
        }

        if(tens.indexOf("DP")!=-1) {
            btnDP.setChecked(true);
            btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));
        }

        if(tens.indexOf("EG")!=-1) {
            btnEG.setChecked(true);
            btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
        }

        if(tens.indexOf("FL")!=-1) {
            btnFL.setChecked(true);
            btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
        }

        if(tens.indexOf("FS")!=-1) {
            btnFS.setChecked(true);
            btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
        }

        if(tens.indexOf("MK")!=-1) {
            btnMK.setChecked(true);
            btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));
        }

        if(tens.indexOf("PE")!=-1) {
            btnPE.setChecked(true);
            btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_c));
        }

        if(tens.indexOf("PK")!=-1) {
            btnPK.setChecked(true);
            btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_c));
        }

        if(tens.indexOf("SB")!=-1) {
            btnSB.setChecked(true);
            btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_c));
        }

        if(tens.indexOf("SF")!=-1) {
            btnSF.setChecked(true);
            btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_c));
        }



    }
    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class Details extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("http://115.91.88.230:9998/mukja/ERoomjoin.do?er_no=%s&username=%s&store_id=%s",er_no,userName,store_id));
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
            Log.i("ERoom", result);

            if(result!=null) {
                try {
                    JSONObject json = new JSONObject(result);
                    int no =Integer.parseInt(json.getString("joinER"));
                    Log.i("가즈아",""+no);
                    int rool = Integer.parseInt(json.getString("selectrool"));
                    Log.i("가즈아",""+rool);
                    if(no==1){
                        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                        builder.setMessage("신청이 완료되었습니다.");
                        builder.setPositiveButton("확인",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                        builder.show();
                    }
                    else{
                        if(rool==0){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                            builder.setMessage("수락 대기중입니다.");
                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            builder.show();
                        }
                        if(rool==1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                            builder.setMessage("이미 가입완료되었습니다.");
                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            builder.show();
                        }
                        if(rool==-1){
                            AlertDialog.Builder builder = new AlertDialog.Builder(ViewDetailsActivity.this);
                            builder.setMessage("거절되었습니다.");
                            builder.setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                        }
                                    });
                            builder.show();
                        }
                    }




                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }

    }///////////////AsyncTask



    private void initView() {
        filterText = (TextView) findViewById(R.id.filterText);
        closeBtn = (Button) findViewById(R.id.close_btn);
        Profile = (ImageView) findViewById(R.id.Profile);
        titles = (TextView) findViewById(R.id.titles);
        contents = (TextView) findViewById(R.id.contents);
        nickname = (TextView) findViewById(R.id.nickname);
        ages = (TextView) findViewById(R.id.ages);
        maxs = (TextView) findViewById(R.id.max);
        date = (TextView) findViewById(R.id.date);
        time = (TextView) findViewById(R.id.time);
        btnBD = (ToggleButton) findViewById(R.id.btnBD);
        btnCW = (ToggleButton) findViewById(R.id.btnCW);
        btnDP = (ToggleButton) findViewById(R.id.btnDP);
        btnEG = (ToggleButton) findViewById(R.id.btnEG);
        btnFL = (ToggleButton) findViewById(R.id.btnFL);
        btnFS = (ToggleButton) findViewById(R.id.btnFS);
        btnMK = (ToggleButton) findViewById(R.id.btnMK);
        btnPE = (ToggleButton) findViewById(R.id.btnPE);
        btnPK = (ToggleButton) findViewById(R.id.btnPK);
        btnSB = (ToggleButton) findViewById(R.id.btnSB);
        btnSF = (ToggleButton) findViewById(R.id.btnSF);
        JoinRoomBtn = (Button) findViewById(R.id.creatRoom_btn);
    }
}
