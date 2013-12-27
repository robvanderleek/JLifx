package jlifx.boblightd;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.Collection;

import jlifx.bulb.Bulb;
import jlifx.bulb.DiscoveryService;
import jlifx.bulb.GatewayBulb;

public class BoblightDaemon {

    public void run() throws Exception {
        GatewayBulb gatewayBulb = DiscoveryService.discoverGatewayBulb();
        final Collection<Bulb> bulbs = DiscoveryService.discoverAllBulbs(gatewayBulb);
        // final Bulb bulb = new Bulb(new byte[] {(byte)0xd0, 0x73, (byte)0xd5, 0x00, (byte)0xb4, (byte)0xf9}, gatewayBulb);
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap server = new ServerBootstrap();
        server.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new BoblightProtocolHandler(bulbs));
                };
            }).option(ChannelOption.SO_BACKLOG, 100);
        ChannelFuture f = server.bind(19333).sync();
        f.channel().closeFuture().sync();
    }

}