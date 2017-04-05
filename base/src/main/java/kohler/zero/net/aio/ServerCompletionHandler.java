package kohler.zero.net.aio;

import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.util.Date;
import java.util.concurrent.ExecutionException;

/**
 * Created by Kohler on 2017/3/25.
 */
public class ServerCompletionHandler implements CompletionHandler<AsynchronousSocketChannel, Server> {
    @Override
    public void completed(AsynchronousSocketChannel result, Server attachment) {
//        当有下一个客户端介入的时候，直接调用server的accept方法，这样反复执行下去，保证多个客户端都可以阻塞
        attachment.assc.accept(attachment, this);
        read(result);
    }

    @Override
    public void failed(Throwable exc, Server attachment) {
        exc.printStackTrace();
    }

    private void read(final AsynchronousSocketChannel asc) {
        ByteBuffer buf = ByteBuffer.allocate(1024);

        asc.read(buf, buf, new CompletionHandler<Integer, ByteBuffer>() {
            @Override
            public void completed(Integer result, ByteBuffer attachment) {
                attachment.flip();
                System.out.println("Server 收到客户端的数据长度为：" + result);

                String resultData = new String(attachment.array()).trim();
                System.out.println("Server 收到客户端的数据信息为:");
                System.out.println(resultData);

                String resp = "hello world !";
                write(asc, resp);
            }

            @Override
            public void failed(Throwable exc, ByteBuffer attachment) {
                exc.printStackTrace();
            }
        });
    }

    private void write(AsynchronousSocketChannel asc, String response) {
        String resp = buildResp(response);
        ByteBuffer buf = ByteBuffer.allocate(1024);
        buf.put(resp.getBytes());
        buf.flip();

        try {
            asc.write(buf).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static final String CRLF = "\r\n";
    private String buildResp(String content) {
        StringBuilder response =new StringBuilder();
        //1)  HTTP协议版本、状态代码、描述
        response.append("HTTP/1.1 200 OK").append(CRLF);
        //2)  响应头(Response Head)
        response.append("Date:").append(new Date()).append(CRLF);
        response.append("Content-Type:text/plain;charset=utf-8").append(CRLF);
        //正文长度 ：字节长度
        response.append("Content-Length:").append(content.getBytes().length).append(CRLF);
        response.append(CRLF);
        response.append(content);

        return response.toString();
    }
}
