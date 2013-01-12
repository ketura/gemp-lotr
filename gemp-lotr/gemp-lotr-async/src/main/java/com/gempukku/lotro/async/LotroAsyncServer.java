package com.gempukku.lotro.async;

import com.gempukku.lotro.common.ApplicationConfiguration;
import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

public class LotroAsyncServer {
    public static void main(String[] server) {
        ChannelFactory factory =
                new NioServerSocketChannelFactory(
                        Executors.newCachedThreadPool(),
                        Executors.newFixedThreadPool(10));


        ServerBootstrap bootstrap = new ServerBootstrap(factory);

        bootstrap.setPipelineFactory(new LotroServerPipelineFactory());

        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);
        bootstrap.bind(new InetSocketAddress(Integer.parseInt(ApplicationConfiguration.getProperty("port"))));
    }
}
