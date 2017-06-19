package kohler.zero.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
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
            sc.configureBlocking(false);
//            sc.bind(addr);
            sc.connect(addr);

            Selector selector = Selector.open();
            sc.register(selector, SelectionKey.OP_CONNECT);

            new Thread(() -> {
                while (true) {
                    try {
                        selector.select();
                        Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();

                        while (iterator.hasNext()) {
                            SelectionKey key = iterator.next();
                            if (key.isConnectable()) {
                                System.out.println("isConnectable");
                                SocketChannel channel = (SocketChannel)key.channel();
//                                channel.connect(addr);
                                channel.finishConnect();
                                channel.register(selector, SelectionKey.OP_WRITE);
                            } else if (key.isWritable()) {
                                System.out.println("isWritable");
                                SocketChannel channel = (SocketChannel)key.channel();

                                Scanner scanner = new Scanner(System.in);
                                new Thread(() -> {
                                    while (scanner.hasNext()) {
                                        String str = scanner.nextLine();
                                        wbuf.put(str.getBytes());
                                        wbuf.flip();
                                        try {
                                            channel.write(wbuf);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        wbuf.clear();
                                    }
                                }).start();

                                channel.register(selector, SelectionKey.OP_READ);
                            } else if (key.isReadable()) {
                                System.out.println("isReadable");

                                SocketChannel channel = (SocketChannel)key.channel();
                                rbuf.clear();
                                channel.read(rbuf);
                                rbuf.flip();

                                byte[] bytes = new byte[rbuf.remaining()];
                                rbuf.get(bytes);
                                System.out.println("resp: " + new String(bytes));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

            /*Scanner scanner = new Scanner(System.in);
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
            }*/
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
