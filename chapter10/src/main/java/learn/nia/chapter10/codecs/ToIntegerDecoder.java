package learn.nia.chapter10.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class ToIntegerDecoder extends ByteToMessageDecoder {
    public void decode(ChannelHandlerContext ctx, ByteBuf in,
                       List<Object> out) {
        if (in.readableBytes() >= 4) {
            out.add(in.readInt());
        }
    }
}
