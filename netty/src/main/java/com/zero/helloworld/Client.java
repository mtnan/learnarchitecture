package com.zero.helloworld;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.util.concurrent.TimeUnit;

/**
 * Created by Kohler on 2017/5/15.
 */
public class Client {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                 .channel(NioSocketChannel.class)
                 .handler(new ChannelInitializer<SocketChannel>() {
                     @Override
                     protected void initChannel(SocketChannel channel) throws Exception {
                         channel.pipeline().addLast(new ClientHandler());
                     }
                 });

        ChannelFuture future = bootstrap.connect("127.0.0.1", 9876).sync();

        future.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));
        TimeUnit.SECONDS.sleep(1);
        future.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));
        TimeUnit.SECONDS.sleep(1);
        future.channel().writeAndFlush(Unpooled.copiedBuffer("hello netty".getBytes()));
        TimeUnit.SECONDS.sleep(1);

        future.channel().closeFuture().sync();

    }
}
