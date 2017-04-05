package kohler.zero.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Scanner;

/**
 * Created by Kohler on 2017/3/25.
 */
public class Client {
    public static void main(String[] args) {
        InetSocketAddress addr = new InetSocketAddress("", 9876);

        SocketChannel sc;

        ByteBuffer wbuf = ByteBuffer.allocate(1024);
        ByteBuffer rbuf = ByteBuffer.allocate(1024);

        try {
            sc = SocketChannel.open();
            sc.connect(addr);

            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String str = scanner.nextLine();
//            wbuf.put("hello server".getBytes());
                wbuf.put(str.getBytes());
                wbuf.flip();
                sc.write(wbuf);
                wbuf.clear();
//
                rbuf.clear();
                sc.read(rbuf);
                rbuf.flip();

                byte[] bytes = new byte[rbuf.remaining()];
                rbuf.get(bytes);
                System.out.println("resp: " + new String(bytes));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
