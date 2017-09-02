package com.leisucn.test.nettydemo.server.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.embedded.EmbeddedChannel;
import static  org.junit.Assert.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

/**
 * Created by SL on 2017/9/2.
 */
public class ToIntegerDecoderTest {

    private final Logger log = LogManager.getFormatterLogger(getClass());

    private ToIntegerDecoder decoder;

    private ToIntegerDecoder2 decoder2;

    private IntegerToStringDecoder decoder3;

    @Before
    public void setup(){
        this.decoder  = new ToIntegerDecoder();
        this.decoder2 = new ToIntegerDecoder2();
        this.decoder3 = new IntegerToStringDecoder();
    }

    @Test
    public void test_decode_01(){
        test_decode(this.decoder);
    }

    @Test
    public void test_decode_02(){
        test_decode(this.decoder2);
    }

    @Test
    public void test_decode_03(){
        test_decode(this.decoder, this.decoder3);
    }

    private void test_decode(ChannelHandler... decoder){
        ByteBuf buf = Unpooled.buffer();
        for (int i = 0; i < 9; i++ ){
            buf.writeInt(i*2);
        }

        ByteBuf input = buf.duplicate();

        EmbeddedChannel channel = new EmbeddedChannel();
        channel.pipeline().addLast(decoder);
        channel.pipeline().addLast(new EchoInboundHandler());

        assertTrue( "channel.writeInbound", channel.writeInbound(input.retain()) );
        assertTrue( channel.finish() );
    }

    class EchoInboundHandler extends ChannelInboundHandlerAdapter{

        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            log.debug("Msg is %s[%s]", msg, msg.getClass().getSimpleName());
            ctx.fireChannelRead(msg);
        }
    }

}
