package com.leisucn.test.nettydemo.server;


import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.oio.OioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

/**
 * Created by sulei on 2017/8/29.
 */
public class PlainNettyServer {

    private static enum MODE{
        SYN, ASYN
    }

    private final Logger logger = LogManager.getFormatterLogger(getClass());

    private EventLoopGroup getEventLoopGroup(MODE mode){
        return MODE.SYN == mode ? new OioEventLoopGroup() : new NioEventLoopGroup();
    }


    private Class<? extends ServerChannel> getServerSocketChannel(MODE mode){
        return MODE.SYN == mode ? OioServerSocketChannel.class : NioServerSocketChannel.class;
    }


    public void serve(int port, MODE mode) throws Exception{
        //final ByteBuf buf = Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8"));
        EventLoopGroup group = getEventLoopGroup(mode);

        try{
            ServerBootstrap bootstrap = new ServerBootstrap();
            bootstrap.group(group)
                    .channel(getServerSocketChannel(mode))
                    .localAddress(new InetSocketAddress(port))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                                @Override
                                public void channelActive(ChannelHandlerContext ctx) throws Exception {
                                    //ctx.writeAndFlush(buf.duplicate())
                                    ctx.writeAndFlush(Unpooled.copiedBuffer("Hi!\r\n", Charset.forName("UTF-8")))
                                    .addListener(ChannelFutureListener.CLOSE);
                                }


                                @Override
                                public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
                                    cause.printStackTrace();
                                }
                            });
                        }
                    });

            ChannelFuture future = bootstrap.bind().sync();
            future.channel().closeFuture().sync();

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception{
        new PlainNettyServer().serve(9099, MODE.SYN);
    }


}
