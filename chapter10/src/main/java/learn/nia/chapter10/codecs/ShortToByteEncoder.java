package learn.nia.chapter10.codecs;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ShortToByteEncoder extends MessageToByteEncoder<Short> {
    public void encode(ChannelHandlerContext ctx, Short message, ByteBuf out) {
        out.writeShort(message);
    }
}
