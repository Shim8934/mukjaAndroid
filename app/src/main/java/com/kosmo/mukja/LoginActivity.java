package com.kosmo.mukja;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kosmo.mukja.content.TabContent2;
import com.kosmo.mukja.fcm.Users;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnFocusChange;

public class LoginActivity extends AppCompatActivity {
    @BindView(R.id.imageView)
    ImageView imageView;
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.signIn)
    Button signIn;
    @BindView(R.id.signUp)
    Button signUp;
    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });

        signIn.setOnClickListener(listener);




        PermissionListener permissionlistener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                Toast.makeText(LoginActivity.this, "Permission Granted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(LoginActivity.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
            }


        };

        TedPermission.with(this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.INTERNET)
                .check();

    }





    //버튼 이벤트 처리]
    private View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("com.kosmo.mukja","username:"+username.getText().toString()+" password:"+password.getText().toString());
            new LoginAsyncTask().execute(
                    "http://"+TabContent2.ipAddr+":8080/mukja/android/json",username.getText().toString(),password.getText().toString());

        }
    };//////////////////OnClickListener

    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class LoginAsyncTask extends AsyncTask<String,Void,String> {

        private AlertDialog progressDialog;
        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
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
                URL url = new URL(String.format("%s?username=%s&password=%s",params[0],params[1],params[2]));
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

            //서버로부터 받은 데이타(JSON형식) 파싱
            //회원이 아닌 경우 빈 문자열
            Log.i("com.kosmo.mukja","result:"+result);
            if(result !=null && result.length()!=0) {//회원인 경우
                try {
                    JSONObject json = new JSONObject(result);

                    String username = json.getString("username");
                    Log.i("com.kosmo.mukja","username:"+username);
                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                    intent.putExtra("username",username);
                    startActivity(intent);
                    SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
                    SharedPreferences.Editor editor =preferences.edit();
                    editor.putString("username",json.getString("username"));
                    editor.putString("password",json.getString("password"));
                    editor.commit();
                    Log.i("MyMarker","로그인시 아이디:"+preferences.getString("username","defaultID"));
                    FirebaseInstanceId.getInstance().getInstanceId()
                            .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                                @Override
                                public void onComplete(@NonNull Task<InstanceIdResult> task) {
                                    if (!task.isSuccessful()) {
                                        Log.w("가즈아", "getInstanceId failed", task.getException());
                                        return;
                                    }
                                    // Get new Instance ID token
                                    String token = task.getResult().getToken();
                                    Log.i("가즈아",token);
                                    String[] key = username.split("@");
                                    firebaseDatabase.getReference(key[0]).setValue(token);
                                    editor.putString("token",token);
                                    editor.commit();
                                }
                            });


                }
                catch(Exception e){e.printStackTrace();}

            }
            else{//회원이 아닌 경우
                Toast.makeText(LoginActivity.this,"아이디와 비번이 일치하지 않아요",Toast.LENGTH_SHORT).show();
            }

            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }

    }///////////////LoginAsyncTask

}

