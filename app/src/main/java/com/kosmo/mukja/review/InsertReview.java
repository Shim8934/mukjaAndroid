package com.kosmo.mukja.review;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.kosmo.mukja.R;

import java.io.File;
import java.util.List;
import gun0912.tedbottompicker.TedBottomPicker;

public class InsertReview extends AppCompatActivity {
    private ImageView iv_image;
    private Uri selectedUri;
    private Context context = InsertReview.this;
    private RequestManager requestManager;
    private ImageView storeClose;
    private EditText reviewTitle;
    private EditText reviewContent;

    private TextView btnSingleShow;
    private ImageView ivImage;
    private LinearLayout reviewWrite;
    private String store_id,user_id;
    private String title;
    private String content;
    private  String root = Environment.getExternalStorageDirectory().getAbsolutePath();

    public static Uri getUriFromPath(ContentResolver cr, String path) {
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();
        Cursor c = cr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null);
        c.moveToNext();
        int id = c.getInt(c.getColumnIndex("_id"));
        Uri uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);
        return uri;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.insert_review);

        storeClose =  findViewById(R.id.store_close);
        reviewTitle =  findViewById(R.id.reviewTitle);
        reviewContent =  findViewById(R.id.reviewContent);
        btnSingleShow =  findViewById(R.id.btn_single_show);
        ivImage =  findViewById(R.id.iv_image);
        reviewWrite =  findViewById(R.id.reviewWrite);


        Intent intent = getIntent();
        store_id = intent.getStringExtra("store_id");


        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        user_id = preferences.getString("username", "defaultID");

        iv_image = findViewById(R.id.iv_image);
        requestManager = Glide.with(this);
        setSingleShowButton();

        reviewWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 title= reviewTitle.getText().toString();
                 content = reviewContent.getText().toString();
                Log.d("MyMarker", "제목:"+title+" 내용:"+content+" 글쓴이:"+user_id+" 가게: " +store_id );

                Uri realPath=getUriFromPath(getContentResolver(),selectedUri.toString());
                Log.i("MyMarker","realpath:"+realPath.toString());
                Log.i("MyMarker","최상위폴더:"+root);

               new ReivewWriteTask().execute();
            }
        });
    }//onCreate

    private void setSingleShowButton() {
        TextView btnSingleShow = findViewById(R.id.btn_single_show);
        btnSingleShow.setOnClickListener(view -> {
            PermissionListener permissionlistener = new PermissionListener() {
                @Override
                public void onPermissionGranted() {
                    TedBottomPicker.with(InsertReview.this)
                            .setSelectedUri(selectedUri)
                            .setPeekHeight(1200)
                            .show(uri -> {
                                Log.d("ted", "uri: " + uri);
                                Log.d("ted", "uri.getPath(): " + uri.getPath());
                                selectedUri = uri;

                                iv_image.setVisibility(View.VISIBLE);

                                requestManager
                                        .load(uri)
                                        .into(iv_image);
                            });
                }
                @Override
                public void onPermissionDenied(List<String> deniedPermissions) {
                    Toast.makeText(InsertReview.this, "Permission Denied\n" + deniedPermissions.toString(), Toast.LENGTH_SHORT).show();
                }
            };
            checkPermission(permissionlistener);
        });
    }//setSingleShowButton


    private void checkPermission(PermissionListener permissionlistener) {
        TedPermission.with(InsertReview.this)
                .setPermissionListener(permissionlistener)
                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                .setPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .check();
    }//checkPermission






    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class ReivewWriteTask extends AsyncTask<String, Void,  List<String>> {
        private AlertDialog progressDialog;

        @Override
        protected void onPreExecute() {
            //프로그래스바용 다이얼로그 생성]
            //빌더 생성 및 다이얼로그창 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setCancelable(false);
            builder.setView(R.xml.progress);
            builder.setMessage("리뷰 작성 중");

            //빌더로 다이얼로그창 생성
            progressDialog = builder.create();
            progressDialog.show();

        }///////////onPreExecute

        @Override
        protected  List<String> doInBackground(String... params) {
            String serverURL="http://192.168.0.6:8080/mukja/Android/insertReview.do";
            List<String> result = uploadMedia(selectedUri.getPath(),serverURL);

        return result;
        }///////////doInBackground

        @Override
        protected void onPostExecute( List<String> result) {

            for(String item:result){

                Log.i("MyMarker","사진업result"+item);

            }





            //다이얼로그 닫기
            if(progressDialog!=null && progressDialog.isShowing())
                progressDialog.dismiss();
        }
    }///////////////AsyncTask




    private List<String> uploadMedia(String filepath, String serverURL) {

        List<String> response=null;
        try {

            String charset = "UTF-8";
            File uploadFile1 = new File(filepath);
            String requestURL = serverURL;

            MultipartUtility multipart = new MultipartUtility(requestURL, charset);

//            multipart.addHeaderField("User-Agent", "CodeJava");
//            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("title", title);
            multipart.addFormField("content", content);
            multipart.addFormField("user_id", user_id);
            multipart.addFormField("store_id", store_id);

            multipart.addFilePart("uploadedfile", uploadFile1);

            response = multipart.finish();

            Log.i("MyMarker", "SERVER REPLIED:");

            for (String line : response) {
                Log.v("MyMarker", "Line : "+line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
