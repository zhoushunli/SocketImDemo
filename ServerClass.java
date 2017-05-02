import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by shunli on 2017/4/19.
 */

public class ServerClass {

    private ArrayList<Socket> socketList;
    private static final int PORT=10000;

    public void initServer() throws Exception {
        socketList=new ArrayList<>();

        ServerSocket mServerSock=new ServerSocket(PORT);
        while (true){
            Socket socket = mServerSock.accept();
            System.out.println("有连接进来"+socket.getInetAddress().getHostName()+
                    "\n"+socket.getInetAddress().getHostAddress());
            socketList.add(socket);
//            readMsg(socket);
            System.out.println("信息读取完毕");
            new Thread(new ServerThread(socket,socketList)).start();
            System.out.println("集合线程开启完毕");
        }
    }
    public void readMsg(Socket socket){
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            int len=0;
            byte[]bs=new byte[1024*3];
            StringBuffer buffer=new StringBuffer();
            while ((len=bufferedInputStream.read(bs))!=-1){
                buffer.append(new String(bs,0,len));
            }
            InetAddress inetAddress = socket.getInetAddress();
            System.out.println("收到来自:"+ inetAddress.getHostAddress()+inetAddress.getHostName() +buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[]args){
        ServerClass serverClass = new ServerClass();
        try {
            serverClass.initServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
