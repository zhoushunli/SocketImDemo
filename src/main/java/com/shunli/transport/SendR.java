package com.shunli.transport;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by shunli on 2017/4/21.
 */
class SendR implements Runnable {

    private PrintStream mWriter;
    private String mMsg;

    public SendR(Socket socket) {
        try {
            mWriter = new PrintStream(socket.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        this.mMsg = msg;
        synchronized (this){
            this.notify();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                mWriter.println(mMsg);
                mWriter.flush();
                synchronized (this) {
                    this.wait();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
