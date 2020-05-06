package com.gempukku.lotro.async;

import com.gempukku.lotro.async.handler.RootUriRequestHandler;
import com.gempukku.polling.LongPollingSystem;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.lang.reflect.Type;
import java.util.Map;

public class GempukkuHttpPipelineFactory implements ChannelPipelineFactory {
    private GempukkuHttpRequestHandler requestHandler;

    public GempukkuHttpPipelineFactory(GempukkuServer gempukkuServer) {
        Map<Type, Object> context = gempukkuServer.getContext();

        LongPollingSystem longPollingSystem = new LongPollingSystem();
        longPollingSystem.start();

        RootUriRequestHandler uriRequestHandler = new RootUriRequestHandler(context, longPollingSystem);

        requestHandler = new GempukkuHttpRequestHandler(context, uriRequestHandler);
    }

    public ChannelPipeline getPipeline() throws Exception {
        // Create a default pipeline implementation.
        ChannelPipeline pipeline = Channels.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //pipeline.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        // Uncomment the following line if you don't want to handle HttpChunks.
        //pipeline.addLast("aggregator", new HttpChunkAggregator(1048576));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        // Remove the following line if you don't want automatic content compression.
        pipeline.addLast("deflater", new HttpContentCompressor());

        pipeline.addLast("handler", requestHandler);
        return pipeline;
    }
}
