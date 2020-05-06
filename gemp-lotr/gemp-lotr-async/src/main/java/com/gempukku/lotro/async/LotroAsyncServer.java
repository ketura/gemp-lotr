package com.gempukku.lotro.async;

import com.gempukku.lotro.common.ApplicationConfiguration;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class LotroAsyncServer {
    public static void main(String[] server) {
        ChannelFactory factory =
                new NioServerSocketChannelFactory(
                        new ThreadPoolExecutor(10, Integer.MAX_VALUE,
                                60L, TimeUnit.SECONDS,
                                new SynchronousQueue<Runnable>()),
                        new ThreadPoolExecutor(30, Integer.MAX_VALUE,
                                60L, TimeUnit.SECONDS,
                                new SynchronousQueue<Runnable>()));


        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        GempukkuServer gempukkuServer = new GempukkuServer();

        bootstrap.setPipelineFactory(new GempukkuHttpPipelineFactory(gempukkuServer));

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(Integer.parseInt(ApplicationConfiguration.getProperty("port"))));
    }
}
