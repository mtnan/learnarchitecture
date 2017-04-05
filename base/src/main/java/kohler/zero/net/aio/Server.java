package kohler.zero.net.aio;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousChannelGroup;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Kohler on 2017/3/25.
 */
public class Server {
    private ExecutorService executor;
    private AsynchronousChannelGroup threadGroup;
    public AsynchronousServerSocketChannel assc;

    public Server(int port) {
        try {
//            创建线程池
            executor = Executors.newCachedThreadPool();
//            创建线程组
            threadGroup = AsynchronousChannelGroup.withCachedThreadPool(executor, 1);
//            创建服务器通道
            assc = AsynchronousServerSocketChannel.open(threadGroup);
//            绑定端口
            assc.bind(new InetSocketAddress(port));

            System.out.println("server start, port : " + port);
//            进行阻塞
            assc.accept(this, new ServerCompletionHandler());
//            阻塞主线程 不让主线程停止
            Thread.sleep(Integer.MAX_VALUE);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Server server = new Server(8765);

    }
}
