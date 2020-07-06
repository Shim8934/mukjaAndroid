package com.kosmo.mukja.fcm;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.gildaswise.horizontalcounter.HorizontalCounter;
import com.kosmo.mukja.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CustomActivity extends AppCompatActivity {

    private View.OnClickListener mPositiveListener;
    private View.OnClickListener mNegativeListener;

    private HorizontalCounter horizontalCounter;
    private Context context;
    private TextView datePicker;
    private TextView timePicker;
    private TextView filters;

    Bundle avoid_codes = new Bundle();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dialog);
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
        horizontalCounter = findViewById(R.id.horizontal_counter);
        datePicker = findViewById(R.id.datepicker);
        timePicker = findViewById(R.id.timepicker);
        //filters = findViewById(R.id.filters);


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
            timePicker.setText((HH + 9) + "시" + mm + "분");
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
                        timePicker.setText(hourOfDay + "시" + minute + "분");
                    }
                }, (HH + 9), mm, true);
                dialog.show();
            }
        });
//        filters.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(context, NEFilter.class);
//                intent.putExtra("avoid_codes", avoid_codes);
//                startActivityForResult(intent, 3000);
//            }
//        });//filter

    }//onCreat

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == -1) {
            this.avoid_codes = data.getBundleExtra("avoid_codes");

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
