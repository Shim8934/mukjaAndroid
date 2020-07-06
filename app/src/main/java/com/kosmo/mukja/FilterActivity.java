package com.kosmo.mukja;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FilterActivity extends AppCompatActivity {
    @BindView(R.id.filterText)
    TextView textView;

    @BindView(R.id.close_btn)
    Button button;

    @BindView(R.id.close_btn2)
    Button close_btn2;

    ToggleButton btnC, btnK, btnY, btnB, btnJ;
    ToggleButton btnBD, btnCW, btnDP, btnEG, btnFL, btnFS, btnMK, btnPE, btnPK, btnSB, btnSF;
    Bundle avoid_codes = new Bundle();
    Bundle prefer_codes = new Bundle();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_filter);

        ButterKnife.bind(this);
        Intent intent = getIntent();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,intent);
                finish();
            }
        });//close1
        close_btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_OK,intent);
                finish();
            }
        });//close2
        btnC = findViewById(R.id.btnC);
        btnK = findViewById(R.id.btnK);
        btnY = findViewById(R.id.btnY);
        btnB = findViewById(R.id.btnB);
        btnJ = findViewById(R.id.btnJ);
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

        prefer_codes = intent.getBundleExtra("prefer_codes");
        avoid_codes = intent.getBundleExtra("avoid_codes");
        //Log.i("MyMarker","받은후 선호코드:"+prefer_codes.size()+" 받은기피 선호코드:"+avoid_codes.size());
        if(prefer_codes.size()==0){
            prefer_codes.putString("BS","BS");
            prefer_codes.putString("CS","CS");
            prefer_codes.putString("HS","HS");
            prefer_codes.putString("YS","YS");
            prefer_codes.putString("JS","JS");
            Log.i("MyMarker","선호코드 크기 0 초기화");
        }

        for(String key:avoid_codes.keySet()){
            Log.i("MyMarker","key:"+key+" val:"+avoid_codes.get(key));
        }


        if(prefer_codes.get("CS")==null) {
            Log.i("MyMarker","CS존재여부 :"+prefer_codes.get("CS"));
            btnC.setChecked(false);
            btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.chi_b));
        }else{
            btnC.setChecked(true);
            btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.chi_c));
        }
        if(prefer_codes.get("BS")==null) {
            btnB.setChecked(false);
            btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.bs_b));
        }else{
            btnB.setChecked(true);
            btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.bs_c));
        }
        if(prefer_codes.get("HS")==null) {
            btnK.setChecked(false);
            btnK.setBackgroundDrawable(getResources().getDrawable(R.drawable.k_b));
        }else{
            btnK.setChecked(true);
            btnK.setBackgroundDrawable(getResources().getDrawable(R.drawable.k_c));
        }
        if(prefer_codes.get("YS")==null) {
            btnY.setChecked(false);
            btnY.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_b));
        }else{
            btnY.setChecked(true);
            btnY.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_c));
        }
        if(prefer_codes.get("JS")==null) {
            btnJ.setChecked(false);
            btnJ.setBackgroundDrawable(getResources().getDrawable(R.drawable.j_b));
        }else{
            btnJ.setChecked(true);
            btnJ.setBackgroundDrawable(getResources().getDrawable(R.drawable.j_c));
        }


        if(avoid_codes.get("BD")!=null) {
            btnBD.setChecked(true);
            btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
        }

        if(avoid_codes.get("CW")!=null) {
            btnCW.setChecked(true);
            btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
        }

        if(avoid_codes.get("DP")!=null) {
            btnDP.setChecked(true);
            btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));
        }

        if(avoid_codes.get("EG")!=null) {
            btnEG.setChecked(true);
            btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
        }

        if(avoid_codes.get("FL")!=null) {
            btnFL.setChecked(true);
            btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
        }

        if(avoid_codes.get("FS")!=null) {
            btnFS.setChecked(true);
            btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
        }

        if(avoid_codes.get("MK")!=null) {
            btnMK.setChecked(true);
            btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));
        }

        if(avoid_codes.get("PE")!=null) {
            btnPE.setChecked(true);
            btnPE.setBackgroundDrawable(getResources().getDrawable(R.drawable.pe_c));
        }

        if(avoid_codes.get("PK")!=null) {
            btnPK.setChecked(true);
            btnPK.setBackgroundDrawable(getResources().getDrawable(R.drawable.pk_c));
        }

        if(avoid_codes.get("SB")!=null) {
            btnSB.setChecked(true);
            btnSB.setBackgroundDrawable(getResources().getDrawable(R.drawable.sb_c));
        }

        if(avoid_codes.get("SF")!=null) {
            btnSF.setChecked(true);
            btnSF.setBackgroundDrawable(getResources().getDrawable(R.drawable.sf_c));
        }


        btnC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnC.isChecked()){
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.chi_c));
                    prefer_codes.putString("CS","CS");

                }else{
                    if(prefer_codes.size()==1) {
                        btnC.setChecked(true);
                        return;
                    }
                    btnC.setBackgroundDrawable(getResources().getDrawable(R.drawable.chi_b));
                    prefer_codes.remove("CS");

                }
            }
        });
        btnK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnK.isChecked()){
                    btnK.setBackgroundDrawable(getResources().getDrawable(R.drawable.k_c));
                    prefer_codes.putString("HS","HS");
                }else{
                    if(prefer_codes.size()==1) {
                        btnK.setChecked(true);
                        return;
                    }
                    btnK.setBackgroundDrawable(getResources().getDrawable(R.drawable.k_b));
                    prefer_codes.remove("HS");
                }
            }
        });
        btnY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnY.isChecked()){
                    btnY.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_c));
                    prefer_codes.putString("YS","YS");
                }else{
                    if(prefer_codes.size()==1) {
                        btnY.setChecked(true);
                        return;
                    }
                    btnY.setBackgroundDrawable(getResources().getDrawable(R.drawable.y_b));
                    prefer_codes.remove("YS");
                }
            }
        });
        btnB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnB.isChecked()){
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.bs_c));
                    prefer_codes.putString("BS","BS");
                }else{
                    if(prefer_codes.size()==1) {
                        btnB.setChecked(true);
                        return;
                    }
                    btnB.setBackgroundDrawable(getResources().getDrawable(R.drawable.bs_b));
                    prefer_codes.remove("BS");
                }
            }
        });

        btnJ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnJ.isChecked()){
                    btnJ.setBackgroundDrawable(getResources().getDrawable(R.drawable.j_c));
                    prefer_codes.putString("JS","JS");
                }else{
                    if(prefer_codes.size()==1) {
                        btnJ.setChecked(true);
                        return;
                    }
                    btnJ.setBackgroundDrawable(getResources().getDrawable(R.drawable.j_b));
                    prefer_codes.remove("JS");
                }
            }
        });

        btnBD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("BD")!=null){
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_b));
                    avoid_codes.remove("BD");
                }else{
                    btnBD.setBackgroundDrawable(getResources().getDrawable(R.drawable.db_c));
                    avoid_codes.putString("BD","BD");
                }
            }
        });
        btnCW.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("CW")!=null){
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_b));
                    avoid_codes.remove("CW");
                }else{
                    btnCW.setBackgroundDrawable(getResources().getDrawable(R.drawable.cw_c));
                    avoid_codes.putString("CW","CW");
                }
            }
        });
        btnDP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("DP")!=null){
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_b));
                    avoid_codes.remove("DP");
                }else{
                    btnDP.setBackgroundDrawable(getResources().getDrawable(R.drawable.dp_c));
                    avoid_codes.putString("DP","DP");
                }
            }
        });
        btnEG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("EG")!=null){
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_b));
                    avoid_codes.remove("EG");
                }else{
                    btnEG.setBackgroundDrawable(getResources().getDrawable(R.drawable.eg_c));
                    avoid_codes.putString("EG","EG");
                }
            }
        });
        btnFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("FL")!=null){
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_b));
                    avoid_codes.remove("FL");
                }else{
                    btnFL.setBackgroundDrawable(getResources().getDrawable(R.drawable.fl_c));
                    avoid_codes.putString("FL","FL");
                }
            }
        });
        btnFS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("FS")!=null){
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_b));
                    avoid_codes.remove("FS");
                }else{
                    btnFS.setBackgroundDrawable(getResources().getDrawable(R.drawable.fs_c));
                    avoid_codes.putString("FS","FS");
                }
            }
        });
        btnMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(avoid_codes.get("MK")!=null){
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_b));
                    avoid_codes.remove("MK");
                }else{
                    btnMK.setBackgroundDrawable(getResources().getDrawable(R.drawable.mk_c));
                    avoid_codes.putString("MK","MK");
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

    }//oncreate


}//class