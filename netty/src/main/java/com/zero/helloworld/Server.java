package com.zero.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.sctp.nio.NioSctpServerChannel;
import io.netty.channel.socket.SocketChannel;

/**
 * Created by Kohler on 2017/5/15.
 */
public class Server {
    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup pGroup = new NioEventLoopGroup();
        NioEventLoopGroup cGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(pGroup, cGroup)
         .channel(NioSctpServerChannel.class)
         .option(ChannelOption.SO_BACKLOG, 1024)
         .option(ChannelOption.SO_SNDBUF, 32 * 1024)
         .option(ChannelOption.SO_RCVBUF, 32 * 1024)
         .option(ChannelOption.SO_KEEPALIVE, true)
         .childHandler(new ChannelInitializer<SocketChannel>() {
             @Override
             protected void initChannel(SocketChannel socketChannel) throws Exception {
                 socketChannel.pipeline().addLast(new ServerHandler());
             }
         });

        ChannelFuture cf1 = b.bind(9876).sync();

        cf1.channel().closeFuture().sync();

        pGroup.shutdownGracefully();
        cGroup.shutdownGracefully();
    }
}
