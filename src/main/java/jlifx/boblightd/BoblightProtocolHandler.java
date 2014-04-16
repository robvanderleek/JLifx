package jlifx.boblightd;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.awt.Color;
import java.io.IOException;
import java.util.Collection;
import java.util.StringTokenizer;

import jlifx.bulb.IBulb;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BoblightProtocolHandler extends ChannelHandlerAdapter {
    private static final Log LOG = LogFactory.getLog(BoblightProtocolHandler.class);
    private final Collection<IBulb> bulbs;

    public BoblightProtocolHandler(Collection<IBulb> bulbs) {
        this.bulbs = bulbs;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf b = (ByteBuf)msg;
        byte buf[] = new byte[b.readableBytes()];
        b.readBytes(buf);
        b.release();
        String messageString = new String(buf);
        String[] messages = messageString.split("\n");
        for (String message : messages) {
            handleMessage(ctx, message);
        }
    }

    private void handleMessage(ChannelHandlerContext ctx, String message) throws IOException {
        if (message.startsWith("hello")) {
            LOG.info("Hello? Hello!");
            sendReplyMessage(ctx, "hello\n");
        } else if (message.startsWith("get version")) {
            sendReplyMessage(ctx, "version 5\n");
        } else if (message.startsWith("get lights")) {
            sendReplyMessage(ctx, "lights 1\nlight center scan 0 100 0 100\n");
        } else if (message.startsWith("ping")) {
            sendReplyMessage(ctx, "ping\n");
        } else if (message.startsWith("set light center rgb")) {
            handleSetLightCenterMessage(message);
        } else {
            LOG.info("Received unknown message: " + message);
        }
    }

    private void handleSetLightCenterMessage(String message) throws IOException {
        String values = message.substring(message.indexOf("rgb") + 4);
        StringTokenizer tokenizer = new StringTokenizer(values);
        float r = Float.parseFloat(tokenizer.nextToken());
        float g = Float.parseFloat(tokenizer.nextToken());
        float b = Float.parseFloat(tokenizer.nextToken());
        for (IBulb bulb : bulbs) {
            bulb.colorize(new Color(r, g, b), 0, 1.0f);
        }
    }

    private void sendReplyMessage(ChannelHandlerContext ctx, String message) {
        byte[] messageBytes = message.getBytes();
        final ByteBuf replyBuffer = ctx.alloc().buffer(messageBytes.length).writeBytes(messageBytes);
        ctx.writeAndFlush(replyBuffer);
    }

}