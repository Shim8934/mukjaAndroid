package com.kosmo.mukja.fcm;

import android.app.AlertDialog;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kosmo.mukja.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class ChatDetailsActivity extends AppCompatActivity {

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
    private String userToken;
    private String master;
    String token;
    String nick;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_chatdetails);
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = (int) (display.getWidth() * 1);
        int height = (int) (display.getHeight() * 0.98);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
        Intent intent = getIntent();
        initView();
        er_no=intent.getIntExtra("er_no",0);
        store_id=intent.getStringExtra("store_id");
        nick=intent.getStringExtra("nick");
        Log.i("가즈아",store_id);

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
