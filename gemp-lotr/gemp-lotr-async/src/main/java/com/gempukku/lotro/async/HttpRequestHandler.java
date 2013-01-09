package com.gempukku.lotro.async;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_LENGTH;
import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.CONTENT_TYPE;
import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import org.jboss.netty.util.CharsetUtil;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

public class HttpRequestHandler extends SimpleChannelUpstreamHandler {

    private HttpRequest _request;
    private boolean _readingChunks;
    private String _uri;
    private Map<Type, Object> _objects;

    public HttpRequestHandler(Map<Type, Object> objects) {
        _objects = objects;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
        _request = (HttpRequest) e.getMessage();

        if (is100ContinueExpected(_request)) {
            send100Continue(e);
        }

        String uri = _request.getUri();

        if (uri.indexOf("?")>-1)
            _uri = uri.substring(0, uri.indexOf("?"));
        else
            _uri = uri;

        QueryStringDecoder queryStringDecoder = new QueryStringDecoder(_request.getUri());
        Map<String, List<String>> queryStringParams = queryStringDecoder.getParameters();

        HttpMethod httpMethod = _request.getMethod();

        if (_request.isChunked()) {
            send400Error(e);
        } else {
            ChannelBuffer content = _request.getContent();
            if (content.readable()) {
            }
            writeResponse(e);
        }
    }

    private void writeResponse(MessageEvent e) {
        // Decide whether to close the connection or not.
        boolean keepAlive = isKeepAlive(_request);

        // Build the response object.
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
        response.setContent(ChannelBuffers.copiedBuffer("Response: " + _uri, CharsetUtil.UTF_8));
        response.setHeader(CONTENT_TYPE, "text/plain; charset=UTF-8");

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
        }

        // Write the response.
        ChannelFuture future = e.getChannel().write(response);

        // Close the non-keep-alive connection after the write operation is done.
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void send400Error(MessageEvent e) {
        boolean keepAlive = isKeepAlive(_request);

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, BAD_REQUEST);
        ChannelFuture future = e.getChannel().write(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void send100Continue(MessageEvent e) {
        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, CONTINUE);
        e.getChannel().write(response);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
            throws Exception {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
