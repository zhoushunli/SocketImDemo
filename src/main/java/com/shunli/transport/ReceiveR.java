package com.shunli.transport;

import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;

/**
 * Created by shunli on 2017/4/21.
 */
class ReceiveR implements Runnable {

    private Socket mSock;
    private BufferedReader reader;
    private OnMsgReceivedListener onMsgReceivedListener;

    public ReceiveR(Socket socket) {
        this.mSock = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(mSock.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String str = null;
        try {
            reader = new BufferedReader(new InputStreamReader(mSock.getInputStream()));
            Log.i("mytag","开始接收了");
            while ((str = reader.readLine()) != null) {
                Log.i("mytag","开始读取了"+str);
                if (onMsgReceivedListener != null) {
                    Message m = new Message();
                    m.obj = str;
                    onMsgReceivedListener.onReceive(m);
                }
            }
            Log.i("mytag","读完了");

        } catch (Exception e) {
            e.printStackTrace();
            Log.i("mytag","异常发了");
        }
    }

    public void setOnMsgReceivedListener(OnMsgReceivedListener onMsgReceivedListener) {
        this.onMsgReceivedListener = onMsgReceivedListener;
    }
}
