package com.gempukku.lotro.async;

import org.apache.log4j.Logger;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.*;
import org.jboss.netty.handler.codec.http.DefaultHttpResponse;
import org.jboss.netty.handler.codec.http.HttpRequest;
import org.jboss.netty.handler.codec.http.HttpResponse;
import org.jboss.netty.handler.codec.http.HttpResponseStatus;
import org.jboss.netty.util.CharsetUtil;
import org.w3c.dom.Document;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.lang.reflect.Type;
import java.util.Map;

import static org.jboss.netty.handler.codec.http.HttpHeaders.Names.*;
import static org.jboss.netty.handler.codec.http.HttpHeaders.is100ContinueExpected;
import static org.jboss.netty.handler.codec.http.HttpHeaders.isKeepAlive;
import static org.jboss.netty.handler.codec.http.HttpResponseStatus.*;
import static org.jboss.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class LotroHttpRequestHandler extends SimpleChannelUpstreamHandler {
    private Logger _log = Logger.getLogger(LotroHttpRequestHandler.class);

    private Map<Type, Object> _objects;
    private UriRequestHandler _uriRequestHandler;

    public LotroHttpRequestHandler(Map<Type, Object> objects, UriRequestHandler uriRequestHandler) {
        _objects = objects;
        _uriRequestHandler = uriRequestHandler;
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, final MessageEvent e) throws Exception {
        final HttpRequest request = (HttpRequest) e.getMessage();

        if (is100ContinueExpected(request)) {
            send100Continue(e);
        }

        String uri = request.getUri();

        if (uri.indexOf("?") > -1)
            uri = uri.substring(0, uri.indexOf("?"));

        if (request.isChunked()) {
            send400Error(request, e);
        } else {
            ResponseWriter responseWriter = new ResponseWriter() {
                @Override
                public void writeError(int status) {
                    writeHttpErrorResponse(request, status, null, e);
                }

                @Override
                public void writeError(int status, Map<String, String> headers) {
                    writeHttpErrorResponse(request, status, headers, e);
                }

                @Override
                public void writeResponse(Document document) {
                    writeHttpResponse(request, document, null, e);
                }

                @Override
                public void writeResponse(Document document, Map<String, String> headers) {
                    writeHttpResponse(request, document, headers, e);
                }

                @Override
                public void writeHtmlResponse(String html) {
                    writeHttpHtmlResponse(request, html, e);
                }

                @Override
                public void writeFile(File file) {
                    writeFileResponse(request, file, e);
                }
            };

            _uriRequestHandler.handleRequest(uri, request, _objects, responseWriter, e);
        }
    }

    private void writeFileResponse(HttpRequest request, File file, MessageEvent e) {
        try {
            RandomAccessFile raf;
            try {
                raf = new RandomAccessFile(file, "r");
            } catch (FileNotFoundException fnfe) {
                writeHttpErrorResponse(request, 404, null, e);
                return;
            }
            long fileLength = raf.length();


            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
            response.setHeader(CONTENT_LENGTH, fileLength);

            String fileName = file.getName();
            if (fileName.endsWith(".html")) {
                response.setHeader(CACHE_CONTROL, "no-cache");
                response.setHeader(PRAGMA, "no-cache");
                response.setHeader(EXPIRES, -1);
                response.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");
            }

            Channel ch = e.getChannel();

            // Write the initial line and the header.
            ch.write(response);

            // Write the content.
            ChannelFuture writeFuture;
            // No encryption - use zero-copy.
            final FileRegion region =
                    new DefaultFileRegion(raf.getChannel(), 0, fileLength);
            writeFuture = ch.write(region);
            writeFuture.addListener(new ChannelFutureProgressListener() {
                public void operationComplete(ChannelFuture future) {
                    region.releaseExternalResources();
                }

                public void operationProgressed(
                        ChannelFuture future, long amount, long current, long total) {
                }
            });

            // Decide whether to close the connection or not.
            if (!isKeepAlive(request)) {
                // Close the connection when the whole content is written out.
                writeFuture.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (IOException exp) {
            writeHttpErrorResponse(request, 500, null, e);
        }
    }

    private void writeHttpErrorResponse(HttpRequest request, int status, Map<String, String> headers, MessageEvent e) {
        boolean keepAlive = isKeepAlive(request);

        HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(status));

        if (headers != null) {
            for (Map.Entry<String, String> header: headers.entrySet())
                response.setHeader(header.getKey(), header.getValue());
        }

        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
        }

        ChannelFuture future = e.getChannel().write(response);
        if (!keepAlive) {
            future.addListener(ChannelFutureListener.CLOSE);
        }
    }

    private void writeHttpResponse(HttpRequest request, Document document, Map<String, String> headers, MessageEvent e) {
        // Decide whether to close the connection or not.
        boolean keepAlive = isKeepAlive(request);

        try {
            // Build the response object.
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

            if (headers != null) {
                for (Map.Entry<String, String> header: headers.entrySet())
                    response.setHeader(header.getKey(), header.getValue());
            }

            int length = 0;
            if (document != null) {
                DOMSource domSource = new DOMSource(document);
                StringWriter writer = new StringWriter();
                StreamResult result = new StreamResult(writer);
                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer transformer = tf.newTransformer();
                transformer.transform(domSource, result);

                String responseString = writer.toString();
                response.setContent(ChannelBuffers.copiedBuffer(responseString, CharsetUtil.UTF_8));
                response.setHeader(CONTENT_TYPE, "application/xml; charset=UTF-8");

                length = responseString.length();
            }

            if (keepAlive) {
                // Add 'Content-Length' header only for a keep-alive connection.
                response.setHeader(CONTENT_LENGTH, length);
            }

            // Write the response.
            ChannelFuture future = e.getChannel().write(response);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception exp) {
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(500));

            if (keepAlive) {
                // Add 'Content-Length' header only for a keep-alive connection.
                response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
            }

            // Write the response.
            ChannelFuture future = e.getChannel().write(response);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }

    }

    private void writeHttpHtmlResponse(HttpRequest request, String html, MessageEvent e) {
        // Decide whether to close the connection or not.
        boolean keepAlive = isKeepAlive(request);

        try {
            // Build the response object.
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);

            response.setContent(ChannelBuffers.copiedBuffer(html, CharsetUtil.UTF_8));
            response.setHeader(CONTENT_TYPE, "text/html; charset=UTF-8");

            int length = html.length();

            if (keepAlive) {
                // Add 'Content-Length' header only for a keep-alive connection.
                response.setHeader(CONTENT_LENGTH, length);
            }

            // Write the response.
            ChannelFuture future = e.getChannel().write(response);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        } catch (Exception exp) {
            HttpResponse response = new DefaultHttpResponse(HTTP_1_1, HttpResponseStatus.valueOf(500));

            if (keepAlive) {
                // Add 'Content-Length' header only for a keep-alive connection.
                response.setHeader(CONTENT_LENGTH, response.getContent().readableBytes());
            }

            // Write the response.
            ChannelFuture future = e.getChannel().write(response);
            if (!keepAlive) {
                future.addListener(ChannelFutureListener.CLOSE);
            }
        }
    }

    private void send400Error(HttpRequest request, MessageEvent e) {
        boolean keepAlive = isKeepAlive(request);

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
        _log.error("Error while processing request", e.getCause());
        e.getChannel().close();
    }
}
