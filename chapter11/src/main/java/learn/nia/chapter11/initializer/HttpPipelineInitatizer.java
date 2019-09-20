package learn.nia.chapter11.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import learn.nia.chapter11.handler.HttpServerHandler;

public class HttpPipelineInitatizer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast(new HttpServerCodec());

        pipeline.addLast(new HttpContentCompressor());

        pipeline.addLast(new HttpObjectAggregator(1048576));
        pipeline.addLast(new ChunkedWriteHandler());
        pipeline.addLast(new HttpServerHandler());

    }
}
