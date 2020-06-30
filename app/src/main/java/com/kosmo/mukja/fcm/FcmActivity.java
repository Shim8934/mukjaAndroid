//package com.kosmo.mukja.fcm;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.AdapterView;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ListView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.kosmo.mukja.R;
//
//import java.util.List;
//import java.util.Vector;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//
//public class FcmActivity extends AppCompatActivity {
//
//    @BindView(R.id.pull_message)
//    ListView pull_message;
//    @BindView(R.id.edt)
//    EditText edt;
//    @BindView(R.id.btn_send)
//    Button btn;
//    String text;
//    //리스트뷰에 뿌려질 데이타  선언]
//    private List<Users> items;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        setContentView(R.layout.listitemchat);
//        ButterKnife.bind(this);
//
//        items = new Vector<Users>();
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                text = edt.getText().toString();
//
//                items.add(new Users());
//                CustomAdapter adapter = new CustomAdapter(FcmActivity.this, items);
//                pull_message = findViewById(R.id.pull_message);
//                pull_message.setAdapter(adapter);
//                pull_message.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(FcmActivity.this, pull_message.getItemAtPosition(position) + " 선택", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//    }
//}