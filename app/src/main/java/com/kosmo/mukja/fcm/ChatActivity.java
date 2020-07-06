package com.kosmo.mukja.fcm;


import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.kosmo.mukja.R;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;


public class ChatActivity extends AppCompatActivity {
    private WebSocketClient webSocketClient;
    private TextView chatView;
    private Button chatSent;
    private EditText chatEdit;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        createWebSocketClient();
        initView();


    }

    private void createWebSocketClient() {
        URI uri;
        try {
            // Connect to local host
            uri = new URI("ws://115.91.88.230:9998/mukja/chat.do");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
                chatSent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("WebSocket", "버튼클릭");
                        Log.i("WebSocket", "내용:"+chatEdit.getText().toString());
                        webSocketClient.send(chatEdit.getText().toString());
                    }
                });

            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Log.i("WebSocket", s);
                          /*  TextView textView = findViewById(R.id.chat_view);
                            textView.setText(message);*/
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onBinaryReceived(byte[] data) {
            }

            @Override
            public void onPingReceived(byte[] data) {
            }

            @Override
            public void onPongReceived(byte[] data) {
            }

            @Override
            public void onException(Exception e) {
                System.out.println(e.getMessage());
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

    private void initView() {

        chatSent = findViewById(R.id.chat_sent);
        chatEdit = findViewById(R.id.chat_edit);
    }
}


