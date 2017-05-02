import java.io.*;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by shunli on 2017/4/21.
 */

public class ServerThread implements Runnable {
    private Socket mSocket;
    private BufferedReader mReader;
    private ArrayList<Socket> mSocketList;

    public ServerThread(Socket socket,ArrayList<Socket> sockets){
        mSocket=socket;
        mSocketList=sockets;
        System.out.println("构造函数走了");
        try {
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("异常了");
        }
    }
    @Override
    public void run() {
        System.out.println("run走了");
        String str=null;
        try {
            while ((str=mReader.readLine())!=null){
                for (Socket socket : mSocketList) {
                    PrintStream outputStream = new PrintStream(socket.getOutputStream());
                    System.out.println(str+"");
                    outputStream.println(str);
                    outputStream.flush();
//                    socket.getOutputStream().close();
//                    socket.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("又异常了。。。");
        }
    }
}
