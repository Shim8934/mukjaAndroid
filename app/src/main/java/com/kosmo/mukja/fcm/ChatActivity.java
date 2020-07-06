package com.kosmo.mukja.fcm;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.kosmo.mukja.R;

import java.io.BufferedReader;
import java.io.PrintWriter;

import io.socket.client.Socket;

public class ChatActivity extends Activity {

    private Socket mSocket;
    EditText input;
    Button button;
    TextView output;
    BufferedReader in;
    PrintWriter out;
    String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        input = findViewById(R.id.chat_edit);
        button = findViewById(R.id.chat_sent);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {

//                     Uri uri = new URI("ws://joker.com/say");
//                     WebSocketClient ws = new WebSocketClient(uri) {
//                         public void onpen(){
//
//                         }
//                     }





            }
        });


    }

}
