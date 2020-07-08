package com.kosmo.mukja.fcm;


import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kosmo.mukja.R;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import tech.gusavila92.websocketclient.WebSocketClient;
//import org.java_websocket.client.;

public class ChatActivity extends AppCompatActivity {
    private WebSocketClient webSocketClient;

    private Context context;
    private RelativeLayout chatLayout;
    private RecyclerView chatListView;
    private LinearLayout bottomlayout;
    private EditText chatEditText1;
    private ImageButton enterChat1;
    private FloatingActionButton moveToDown;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatroom);
        createWebSocketClient();

        initView();
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
                Log.i("WebSocket", "Session is starting");
                webSocketClient.send("Hello World!");
                enterChat1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("WebSocket", "버튼클릭");
                        Log.i("WebSocket", "내용:" + chatEditText1.getText().toString());

                        webSocketClient.send(chatEditText1.getText().toString());
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
        chatLayout = findViewById(R.id.chat_layout);
        chatListView = findViewById(R.id.chat_list_view);
        bottomlayout = findViewById(R.id.bottomlayout);
        chatEditText1 = findViewById(R.id.chat_edit_text1);
        enterChat1 = findViewById(R.id.enter_chat1);
        moveToDown = findViewById(R.id.move_to_down);
    }
}



