package com.kosmo.mukja.fcm;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.kosmo.mukja.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

import tech.gusavila92.websocketclient.WebSocketClient;

public class ChatActivity extends AppCompatActivity {

    WebSocketClient webSocketClient;
    String imageurl = null;
    String mastername = "";
    String masternick = "";
    String masterimg;
    String username;
    String usernick;
    String userimg;
    int erno;
    int ercno;
    private AppBarLayout barLayout;
    private Toolbar toolbar;
    private ImageButton chatback;
    private TextView title;
    private RecyclerView recyclerView;
    private RelativeLayout bottom;
    private EditText textSend;
    private ImageButton btnSend;
    private MessageAdapter messageAdapter;
    private List<Chat> mChat = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        barLayout = findViewById(R.id.bar_layout);
        toolbar = findViewById(R.id.toolbar);
        chatback = findViewById(R.id.chatback);
        title = findViewById(R.id.title);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        bottom = findViewById(R.id.bottom);
        textSend = findViewById(R.id.text_send);
        btnSend = findViewById(R.id.btn_send);
        Intent intent = getIntent();
        masternick = intent.getStringExtra("nick");
        title.setText(masternick);
        chatback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        erno=intent.getIntExtra("er_no",0);
        ercno=intent.getIntExtra("erc_no",0);

        ChatActivity.Save save = new ChatActivity.Save();
        save.execute();

        createWebSocketClient();
        messageAdapter = new MessageAdapter(ChatActivity.this, mChat, imageurl);
        recyclerView.setAdapter(messageAdapter);
        SharedPreferences preferences = getSharedPreferences("loginInfo",MODE_PRIVATE);
        username = preferences.getString("username",null);
        usernick = preferences.getString("nick",null);
        userimg = preferences.getString("img",null);


    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://192.168.0.6:8080/mukja/chat.do");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("가즈아", "Session is starting");
                btnSend.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("가즈아", "버튼클릭");
                        Log.i("가즈아", "내용:" + textSend.getText().toString());

                       String value = textSend.getText().toString();
                        String msg = username+"/"+ercno+"/msg:"+usernick+":"+value;
                        Log.i("가즈아",msg);
                        if (!msg.equals("")) {
                            Chat chat = new Chat();
                            chat.setDetachNo(1);
                            chat.setUsername(username);
                            chat.setErcno(ercno);
                            chat.setMessage(textSend.getText().toString());
                            mChat.add(chat);
                            messageAdapter.notifyDataSetChanged();
                            recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                            textSend.setText("");
                            webSocketClient.send(msg);
                        } else {
                            Toast.makeText(ChatActivity.this, "You can't send empty message", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("가즈아", "Message received");
                final String message = s;
                Log.i("가즈아", message);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String[] t = message.split("/");
                            Log.i("가즈아",t[0]);
                            Log.i("가즈아",t[1]);
                            Log.i("가즈아",t[2]);
                            String[] a = t[2].split("msg:");
                            Log.i("가즈아",a[1]);
                            int eno=Integer.parseInt(t[1]);
                            if(eno==ercno) {
                                Chat chat = new Chat();
                                chat.setDetachNo(0);
                                chat.setUsername(t[0]);
                                chat.setErcno(eno);
                                chat.setMessage(a[1]);
                                mChat.add(chat);
                                messageAdapter.notifyDataSetChanged();
                                recyclerView.scrollToPosition(messageAdapter.getItemCount()-1);
                            }

                            //ChatActivity.Save asyncTask = new ChatActivity.Save();
                            //asyncTask.execute();
                            Log.i("가즈아", "onTextReceived");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
                Log.i("가즈아", "onBinaryReceived");
            }

            @Override
            public void onPingReceived(byte[] data) {
                Log.i("가즈아", "onPingReceived");
            }

            @Override
            public void onPongReceived(byte[] data) {
                Log.i("가즈아", "onPongReceived");
            }

            @Override
            public void onException(Exception e) {
                Log.i("가즈아", "onException");
                Log.i("가즈아", "" + e);
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };
        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }

    //저장메세지 가져오기
    private class Save extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuffer buf = new StringBuffer();

            try {
                URL url = new URL(String.format("http://115.91.88.230:9998/mukja/ERoomold.do?erc_no=%s&er_no=%s",ercno,erno));
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
            Log.i("가즈아", result);
            if (result != null) {

                for (int i=0;i<result.length();i++){
                    if(result.contains("left")){


                    }

                }

            }
        }
    }
}









