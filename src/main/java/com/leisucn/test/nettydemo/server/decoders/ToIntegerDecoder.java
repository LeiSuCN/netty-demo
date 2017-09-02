package com.leisucn.test.nettydemo.server.decoders;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * Created by SL on 2017/9/2.
 */
public class ToIntegerDecoder extends ByteToMessageDecoder {

    private Logger logger = LogManager.getFormatterLogger(getClass());

    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        if( in.readableBytes() >= 4 ){
            int i = in.readInt();
            out.add(i);
            logger.debug("decode read %d", i);
        }

    }
}
