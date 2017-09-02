package com.leisucn.test.nettydemo.server.decoders;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;

/**
 * Created by SL on 2017/9/3.
 */
public class IntegerToStringDecoder extends MessageToMessageDecoder<Integer> {


    @Override
    protected void decode(ChannelHandlerContext ctx, Integer msg, List<Object> out) throws Exception {
        out.add(String.valueOf(msg));
    }
}
