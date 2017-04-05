package kohler.zero.net.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;

/**
 * Created by Kohler on 2017/3/25.
 * !!!!! buffer操作完都要flip，不然要日狗
 */
public class Server implements Runnable {
    private Selector selector;

    private ByteBuffer readBuf = ByteBuffer.allocate(1024);
    private ByteBuffer writeBuf = ByteBuffer.allocate(1024);

    public Server(int port) {
        try {
//            1 打开复用器
            this.selector = Selector.open();
//            2 打开服务器通道
            ServerSocketChannel channel = ServerSocketChannel.open();
//            3 设置服务器通道为非阻塞模式
            channel.configureBlocking(false);
//            4 绑定地址
            channel.bind(new InetSocketAddress(port));
//            5 把服务器通道注册到多路复用器上，并且监听阻塞事件
            channel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server start, port: " + port);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
//                开始监听
                this.selector.select();
//                返回
                Iterator<SelectionKey> keys = this.selector.selectedKeys().iterator();

                while (keys.hasNext()) {
                    SelectionKey key = keys.next();
                    keys.remove();

                    if (key.isValid()) {
//                        阻塞的状态
                        if (key.isAcceptable()) {
                            this.accept(key);
                        }
//                        可读状态
                        if (key.isReadable()) {
                            this.read(key);
                            SocketChannel channel = (SocketChannel)key.channel();
                            //注册新的事件
                            channel.register(selector, SelectionKey.OP_WRITE);
                        }
//                        可写状态
                        if (key.isWritable()) {
                            this.write(key);
                            SocketChannel channel = (SocketChannel)key.channel();
                            //注册新的事件
                            channel.register(selector, SelectionKey.OP_READ);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void accept(SelectionKey key) {
        try {
//            获取服务通道
            ServerSocketChannel channel = (ServerSocketChannel) key.channel();
//            执行阻塞方法（等待客户端的通道）
            SocketChannel sc = channel.accept();
//            设置阻塞模式
            sc.configureBlocking(false);
//            注册到多路复用器上，并设置读取标示
            sc.register(this.selector, SelectionKey.OP_READ);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void read(SelectionKey key) {

        try {
            this.readBuf.clear();

            SocketChannel channel = (SocketChannel) key.channel();
            int count = channel.read(readBuf);

            if (count < 0) {
                key.channel().close();
                key.cancel();
                return;
            }

            this.readBuf.flip();
            byte[] bytes = new byte[this.readBuf.remaining()];
            this.readBuf.get(bytes);

            System.out.println("requset: " + new String(bytes).trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        writeBuf.clear();
        writeBuf.put("give you some resp".getBytes());
//        超你妈啊，每次都要flip，不然传过去的都是0
        writeBuf.flip();

        try {
            channel.write(writeBuf);
            System.out.println("write");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Thread(new Server(9876)).start();
    }
}
