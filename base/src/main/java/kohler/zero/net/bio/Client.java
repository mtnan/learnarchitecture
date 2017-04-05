package kohler.zero.net.bio;

import java.io.*;
import java.net.Socket;

/**
 * Created by Kohler on 2017/3/25.
 */
public class Client {
//    try, catch, finally ...
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1", 8765);
        OutputStream outputStream = socket.getOutputStream();

        outputStream.write("hello server".getBytes());

        InputStream inputStream = socket.getInputStream();
        String response = new BufferedReader(new InputStreamReader(inputStream)).readLine();
        System.out.println("response: " + response);

        inputStream.close();
        outputStream.close();
        socket.close();
    }
}
