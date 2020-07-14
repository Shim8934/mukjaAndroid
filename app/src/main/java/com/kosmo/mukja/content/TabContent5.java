package com.kosmo.mukja.content;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kosmo.mukja.R;
import com.kosmo.mukja.fcm.ChattingListAdapter;
import com.kosmo.mukja.fcm.ERDTO;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

//1]Fragement상속
//※androidx.fragment.app.Fragment 상속
public class TabContent5 extends Fragment {
    //2]onCreateView()오버 라이딩
    private Context context;
    private String userName;
    private ArrayList<ERDTO> arrayList = new ArrayList<ERDTO>();
    private ERDTO erdto;
    private ImageView myerClose;
    private ListView MyERListView;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activitiy_chattinglist, container, false);
        SharedPreferences preferences = context.getSharedPreferences("loginInfo", context.MODE_PRIVATE);
        userName = preferences.getString("username", null);
        MainAsyncTask mainAsyncTask = new MainAsyncTask();
        mainAsyncTask.execute();
        Log.i("가즈아", "이거:" + userName);
        MyERListView = view.findViewById(R.id.MyER_listView);
        return view;
    }



    //서버로 데이타 전송 및 응답을 받기 위한 스레드 정의
    private class MainAsyncTask extends AsyncTask<String, Void, String> {
        private AlertDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }///////////onPreExecute

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();
            try {

                URL url = new URL(String.format("http://115.91.88.230:9998/mukja/chattingList.do?username=%s", userName));
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
            if (result != null) {
                JsonParser jsonParser = new JsonParser();
                JsonArray chatting = (JsonArray) jsonParser.parse(result);
                for (int i = 0; i < chatting.size(); i++) {
                    JsonObject chattingInfo = (JsonObject) chatting.get(i);
                    ERDTO erdto = new ERDTO();
                    erdto.setStore_id(chattingInfo.get("store_id").toString().replaceAll("\"", ""));
                    erdto.setEr_time(chattingInfo.get("er_time").toString().replaceAll("\"", ""));
                    erdto.setEr_no(Integer.parseInt(chattingInfo.get("er_no").toString().replaceAll("\"", "")));
                    erdto.setErc_no(Integer.parseInt(chattingInfo.get("erc_no").toString().replaceAll("\"", "")));
                    erdto.setEr_master(chattingInfo.get("er_master").toString().replaceAll("\"", ""));
                    erdto.setMaster_nick(chattingInfo.get("master_nick").toString().replaceAll("\"", ""));
                    erdto.setEr_tend(chattingInfo.get("er_tend").toString().replaceAll("\"", ""));
                    erdto.setStore_name(chattingInfo.get("store_name").toString().replaceAll("\"", ""));
                    erdto.setEr_title(chattingInfo.get("er_title").toString().replaceAll("\"", ""));
                    erdto.setEr_content(chattingInfo.get("er_content").toString().replaceAll("\"", "").replace("<p>", "").replace("</p>", ""));
                    erdto.setEr_max(chattingInfo.get("er_max").toString().replaceAll("\"", ""));
                    erdto.setMaster_img(chattingInfo.get("master_img").toString().replaceAll("\"", ""));
                    arrayList.add(erdto);
                }
                showChatList();
            }
        }

    }///////////////AsyncTask

    private void showChatList() {
        // 리스트 어댑터 생성 및 세팅
        ChattingListAdapter chattingListAdapter = new ChattingListAdapter(context, arrayList);
        MyERListView.setAdapter(chattingListAdapter);
        chattingListAdapter.notifyDataSetChanged();
    }

}
