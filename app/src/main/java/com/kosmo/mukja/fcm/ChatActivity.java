package com.kosmo.mukja.fcm;


import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.kosmo.mukja.R;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;

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
        output = findViewById(R.id.chat_view);
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
    // Socket서버에 connect 된 후, 서버로부터 전달받은 'Socket.EVENT_CONNECT' Event 처리.
    private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            Log.i("가즈아","여기");
        }
    };
    // 서버로부터 전달받은 'chat-message' Event 처리.
    private Emitter.Listener onMessageReceived = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            // 전달받은 데이터는 아래와 같이 추출할 수 있습니다.
            JSONObject receivedData = (JSONObject) args[0];
            Log.i("가즈아","여기1");
        }
    };



}



//        extends AppCompatActivity {
//
//    private String CHAT_NAME;
//    private String USER_NAME;
//
//    private ListView chat_view;
//    private EditText chat_edit;
//    private Button chat_send;

//    private FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
//    private DatabaseReference databaseReference = firebaseDatabase.getReference();

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);

//        // 위젯 ID 참조
//        chat_view = (ListView) findViewById(R.id.chat_view);
//        chat_edit = (EditText) findViewById(R.id.chat_edit);
//        chat_send = (Button) findViewById(R.id.chat_sent);
//
//        // 로그인 화면에서 받아온 채팅방 이름, 유저 이름 저장
//        Intent intent = getIntent();
//        CHAT_NAME = intent.getStringExtra("chatName");
//        USER_NAME = intent.getStringExtra("userName");
//
//        // 채팅 방 입장
//        openChat(CHAT_NAME);
//
//        // 메시지 전송 버튼에 대한 클릭 리스너 지정
//        chat_send.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (chat_edit.getText().toString().equals(""))
//                    return;
//
//                ChatDTO chat = new ChatDTO(USER_NAME, chat_edit.getText().toString()); //ChatDTO를 이용하여 데이터를 묶는다.
//                //databaseReference.child("chat").child(CHAT_NAME).push().setValue(chat); // 데이터 푸쉬
//                chat_edit.setText(""); //입력창 초기화
//
//            }
//        });
//    }
//
//    private void addMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
//        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
//        adapter.add(chatDTO.getUserName() + " : " + chatDTO.getMessage());
//    }
//
//    private void removeMessage(DataSnapshot dataSnapshot, ArrayAdapter<String> adapter) {
//        ChatDTO chatDTO = dataSnapshot.getValue(ChatDTO.class);
//        adapter.remove(chatDTO.getUserName() + " : " + chatDTO.getMessage());
//    }
//
//    private void openChat(String chatName) {
//        // 리스트 어댑터 생성 및 세팅
//        final ArrayAdapter<String> adapter
//
//                = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
//        chat_view.setAdapter(adapter);

//        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리
//        databaseReference.child("chat").child(chatName).addChildEventListener(new ChildEventListener() {
//            @Override
//            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                addMessage(dataSnapshot, adapter);
//                Log.e("LOG", "s:"+s);
//            }
//
//            @Override
//            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onChildRemoved(DataSnapshot dataSnapshot) {
//                removeMessage(dataSnapshot, adapter);
//            }
//
//            @Override
//            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });

