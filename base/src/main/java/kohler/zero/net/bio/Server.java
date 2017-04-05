package kohler.zero.net.bio;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Kohler on 2017/3/24.
 */
public class Server {
    public static final int port = 8765;

//    try, catch, finally ...
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(port);
        System.out.println("server start");
        Socket socket = server.accept();

        System.out.println("have client accepted: " + socket.getInetAddress());
        InputStream inputStream = socket.getInputStream();
        byte[] bytes = new byte[1024];
        inputStream.read(bytes);
        System.out.println("client request: " + new String(bytes));

        OutputStream outputStream = socket.getOutputStream();
        outputStream.write("hello client".getBytes());

        inputStream.close();
        outputStream.close();
        server.close();
    }
}
