package jlifx.boblightd;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public final class NetworkUtils {
    private NetworkUtils() {
    }

    public static ChannelFuture startTcpServer(int port, ChannelHandlerAdapter channelHandlerAdapter)
        throws InterruptedException {
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(channelHandlerAdapter);
                }
            })
            .option(ChannelOption.SO_BACKLOG, 100);
        return server.bind(port).sync();
    }

    public static ChannelFuture startUdpServer(int port, ChannelHandler channelHandler) throws InterruptedException {
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap server = new Bootstrap();
        server.group(eventLoopGroup).channel(NioDatagramChannel.class).handler(channelHandler);
        return server.bind(port).sync();
    }
}