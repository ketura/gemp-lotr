package com.gempukku.lotro.async;

import com.gempukku.lotro.async.handler.RootUriRequestHandler;
import com.gempukku.lotro.common.ApplicationConfiguration;
import com.gempukku.polling.LongPollingSystem;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolConfig;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;

public class LotroAsyncServer {
    public static void main(String[] server) throws InterruptedException {
        int httpPort = Integer.parseInt(ApplicationConfiguration.getProperty("port"));

        GempukkuServer gempukkuServer = new GempukkuServer();

        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            LongPollingSystem longPollingSystem = new LongPollingSystem();
            longPollingSystem.start();

            RootUriRequestHandler uriRequestHandler = new RootUriRequestHandler(gempukkuServer.getContext(), longPollingSystem);

            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch)
                                throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();

                            String websocketPath = "/gemp-lotr-websocket";

                            // HTTP coding and request assembly
                            pipeline.addLast(new HttpServerCodec());
                            pipeline.addLast(new HttpObjectAggregator(Short.MAX_VALUE));

                            // Compression
                            pipeline.addLast(new HttpContentCompressor());

                            // Websocket protocol
                            WebSocketServerProtocolConfig config = WebSocketServerProtocolConfig.newBuilder()
                                    .websocketPath(websocketPath)
                                    .checkStartsWith(true).build();
                            pipeline.addLast(new WebSocketServerProtocolHandler(config));

                            // HTTP handling
                            pipeline.addLast(new GempukkuHttpRequestHandler(gempukkuServer.getContext(),
                                    uriRequestHandler));

                            // Websocket handling
                            pipeline.addLast(new GempukkuWebsocketHandler(websocketPath, gempukkuServer.getContext()));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.TCP_NODELAY, true);

            ChannelFuture bind = b.bind(httpPort);
            bind.sync().channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
