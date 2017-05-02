package com.shunli.transport;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText inputText;
    private TextView received;
    private static final String URL="192.168.2.104";
    private static final int PORT=10000;
    private Socket userSock;
    private SendR sendR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            initSocket();
        } catch (Exception e) {
            Toast.makeText(this,"连接失败",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
        initView();
    }

    private void initView() {
        Button send = (Button) findViewById(R.id.send);
        inputText = (EditText) findViewById(R.id.input_text);
        received = (TextView) findViewById(R.id.received);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendR.sendMsg(inputText.getText().toString());
            }
        });
    }

    private void initSocket() throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    userSock = new Socket(URL,PORT);
                    sockHandler.sendEmptyMessage(0);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private Handler sockHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            ReceiveR receiveR = new ReceiveR(userSock);
            new Thread(receiveR).start();
            Log.i("mytag","线程开始了");
            receiveR.setOnMsgReceivedListener(new OnMsgReceivedListener() {
                @Override
                public void onReceive(Message message) {
                    handler.sendMessage(message);
                }
            });

            sendR = new SendR(userSock);
            new Thread(sendR).start();
        }
    };
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String s = msg.obj.toString();
            received.setText(s);
        }
    };

}
