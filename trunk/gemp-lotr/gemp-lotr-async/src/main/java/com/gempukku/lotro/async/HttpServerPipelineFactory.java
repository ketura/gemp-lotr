package com.gempukku.lotro.async;

import com.gempukku.lotro.packs.PacksStorage;
import com.gempukku.lotro.server.provider.DaoBuilder;
import com.gempukku.lotro.server.provider.PacksStorageBuilder;
import com.gempukku.lotro.server.provider.ServerBuilder;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.handler.codec.http.HttpContentCompressor;
import org.jboss.netty.handler.codec.http.HttpRequestDecoder;
import org.jboss.netty.handler.codec.http.HttpResponseEncoder;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class HttpServerPipelineFactory implements ChannelPipelineFactory {
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

        Map<Type, Object> objects = new HashMap<Type, Object>();
        objects.put(PacksStorage.class, PacksStorageBuilder.createPacksStorage());
        DaoBuilder.fillObjectMap(objects);
        ServerBuilder.fillObjectMap(objects);
        ServerBuilder.constructObjects(objects);

        pipeline.addLast("handler", new HttpRequestHandler(objects));
        return pipeline;
    }
}
