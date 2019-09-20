package learn.nia.chapter10.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class ToIntegerDecoder2 extends ReplayingDecoder<Void> {
    public void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(in.readInt());
    }
}
