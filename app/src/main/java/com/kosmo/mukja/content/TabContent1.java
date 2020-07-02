package com.kosmo.mukja.content;


import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;

import com.gildaswise.horizontalcounter.HorizontalCounter;
import com.kosmo.mukja.R;
import com.kosmo.mukja.fcm.NEFilter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent1 extends Fragment {

    private HorizontalCounter horizontalCounter;
    private Context context;
    private TextView datePicker;
    private TextView timePicker;
   // private TextView filters;

    Bundle avoid_codes = new Bundle();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.custom_dialog, container, false);
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
        Log.i("가즈아",(HH+9)+"시"+mm+"분");
        horizontalCounter = view.findViewById(R.id.horizontal_counter);
        datePicker = view.findViewById(R.id.datepicker);
        timePicker = view.findViewById(R.id.timepicker);
        //filters = view.findViewById(R.id.filters);



        //클릭 리스너 셋팅 (클릭버튼이 동작하도록 만들어줌.)
        //mPositiveButton.setOnClickListener(mPositiveListener);
        //mNegativeButton.setOnClickListener(mNegativeListener);

        horizontalCounter.setOnReleaseListener(() -> {
            Toast.makeText(context, "Value updated to: " + horizontalCounter.getCurrentValue(), Toast.LENGTH_SHORT).show();
        });
        if (datePicker.getText() == "") {
            datePicker.setText(yyyy + "년" + MM + "월" + dd + "일");
        }
        if (timePicker.getText() == "") {
            timePicker.setText((HH+9)+"시"+mm+"분");
        }

        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(context, datelistener, yyyy, MM - 1, dd);
                dialog.show();
            }
        });
        timePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        timePicker.setText(hourOfDay+"시"+minute+"분");
                    }
                },(HH+9),mm,true);
                dialog.show();
            }
        });


//        filters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, NEFilter.class);
//                intent.putExtra("avoid_codes", avoid_codes);
//                startActivityForResult(intent,3000);
//            }
//        });//filter

        return view;
    }//onCreat

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == -1){
            this.avoid_codes = data.getBundleExtra("avoid_codes");

            String NoE = avoid_codes.get("BD")+"/"+avoid_codes.get("CW")+"/"+avoid_codes.get("DP")+"/"+avoid_codes.get("EG")+"/"+avoid_codes.get("FL")
                    +"/"+avoid_codes.get("MK")+"/"+avoid_codes.get("PE")+"/"+avoid_codes.get("PK")+"/"+avoid_codes.get("SB")+"/"+avoid_codes.get("SF");
            NoE =NoE.replaceAll("/null","");
            NoE =NoE.replaceAll("null","");

            if(NoE.equals("BD")){

            }

            //filters.setText(NoE);

        }

    }
    private DatePickerDialog.OnDateSetListener datelistener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            datePicker.setText(year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일");
            Toast.makeText(context, year + "년" + (monthOfYear + 1) + "월" + dayOfMonth + "일", Toast.LENGTH_SHORT).show();
        }
    };


}




