package com.gempukku.lotro.async;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoom;
import com.gempukku.lotro.chat.ChatRoomListener;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.chat.MessagesAndUsers;
import com.gempukku.lotro.db.IgnoreDAO;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.service.LoggedUserHolder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.w3c.dom.Document;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;

public class GempukkuWebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final LoggedUserHolder loggedUserHolder;
    private final PlayerDAO playerDao;
    private final ChatServer chatServer;
    private final IgnoreDAO ignoreDao;

    private String websocketPath;

    private String websocketType;
    private String websocketIdentifier;
    private String playerId;
    private boolean admin;

    public GempukkuWebsocketHandler(String websocketPath, Map<Type, Object> context) {
        this.websocketPath = websocketPath;
        loggedUserHolder = (LoggedUserHolder) context.get(LoggedUserHolder.class);
        playerDao = (PlayerDAO) context.get(PlayerDAO.class);
        ignoreDao = (IgnoreDAO) context.get(IgnoreDAO.class);
        chatServer = (ChatServer) context.get(ChatServer.class);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = handshake.requestUri();
            HttpHeaders headers = handshake.requestHeaders();

            processWebsocketConnected(ctx, uri, headers);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        if (websocketType != null && websocketType.equals("chat")) {
            ChatRoom chatRoom = chatServer.getChatRoom(websocketIdentifier);
            if (chatRoom != null) {
                chatRoom.postMessage(playerId, msg.text(), admin);
            } else {
                ctx.close();
            }
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (websocketType != null && websocketType.equals("chat")) {
            ChatRoom chatRoom = chatServer.getChatRoom(websocketIdentifier);
            if (chatRoom != null) {
                chatRoom.partChatRoom(playerId);
            }
        }
    }

    private void processWebsocketConnected(ChannelHandlerContext ctx, String uri, HttpHeaders headers) throws HttpProcessingException, ParserConfigurationException, TransformerException {
        String urlAfterContext = uri.substring(websocketPath.length());
        if (urlAfterContext.startsWith("/chat/")) {
            String room = URLDecoder.decode(urlAfterContext.substring(6));

            QueryStringDecoder queryDecoder = new QueryStringDecoder(uri);
            String participantId = getQueryParameterSafely(queryDecoder, "participantId");

            Player resourceOwner = getResourceOwnerSafely(headers, participantId);

            ChatRoom chatRoom = chatServer.getChatRoom(room);
            if (chatRoom == null)
                ctx.close();
            try {
                websocketType = "chat";
                websocketIdentifier = room;

                admin = resourceOwner.getType().contains("a");
                playerId = resourceOwner.getName();

                Set<String> ignoredUsers = ignoreDao.getIgnoredUsers(playerId);
                WebsocketChatRoomListener chatRoomListener = new WebsocketChatRoomListener(chatRoom, admin,
                        ignoredUsers, ctx);
                MessagesAndUsers messagesAndUsers = chatRoom.joinChatRoom(playerId, admin, chatRoomListener);

                Document doc = XMLSerializeUtil.serializeChatRoomData(playerDao, room, messagesAndUsers.getChatMessages(),
                        filterIgnoredUsers(ignoredUsers, messagesAndUsers.getUsers()));
                writeXmlFrame(ctx, doc);
            } catch (PrivateInformationException exp) {
                ctx.close();
            }
        } else {
            // Unknown websocket URL
            ctx.close();
        }
    }

    private void writeXmlFrame(ChannelHandlerContext ctx, Document doc) throws TransformerException {
        DOMSource domSource = new DOMSource(doc);
        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer transformer = tf.newTransformer();
        transformer.transform(domSource, result);

        ctx.write(new TextWebSocketFrame(writer.toString()));
        ctx.flush();
    }

    private String getLoggedUser(HttpHeaders headers) {
        ServerCookieDecoder cookieDecoder = ServerCookieDecoder.STRICT;
        String cookieHeader = headers.get(COOKIE);
        if (cookieHeader != null) {
            Set<Cookie> cookies = cookieDecoder.decode(cookieHeader);
            for (Cookie cookie : cookies) {
                if (cookie.name().equals("loggedUser")) {
                    String value = cookie.value();
                    if (value != null) {
                        return loggedUserHolder.getLoggedUser(value);
                    }
                }
            }
        }
        return null;
    }

    private final Player getResourceOwnerSafely(HttpHeaders headers, String participantId) throws HttpProcessingException {
        String loggedUser = getLoggedUser(headers);
        if (isTest() && loggedUser == null)
            loggedUser = participantId;

        if (loggedUser == null)
            throw new HttpProcessingException(401);

        Player resourceOwner = playerDao.getPlayer(loggedUser);

        if (resourceOwner == null)
            throw new HttpProcessingException(401);

        if (resourceOwner.getType().contains("a") && participantId != null && !participantId.equals("null") && !participantId.equals("")) {
            resourceOwner = playerDao.getPlayer(participantId);
            if (resourceOwner == null)
                throw new HttpProcessingException(401);
        }
        return resourceOwner;
    }

    private String getQueryParameterSafely(QueryStringDecoder queryStringDecoder, String parameterName) {
        List<String> parameterValues = queryStringDecoder.parameters().get(parameterName);
        if (parameterValues != null && parameterValues.size() > 0)
            return parameterValues.get(0);
        else
            return null;
    }

    private Collection<String> filterIgnoredUsers(Collection<String> ignoredUsers, Collection<String> users) {
        List<String> result = new LinkedList<>();
        for (String user : users) {
            if (!ignoredUsers.contains(user))
                result.add(user);
        }
        return result;
    }

    private boolean isTest() {
        return Boolean.valueOf(System.getProperty("test"));
    }

    private class WebsocketChatRoomListener implements ChatRoomListener {
        private ChatRoom chatRoom;
        private boolean admin;
        private Collection<String> ignoredUsers;
        private ChannelHandlerContext ctx;

        public WebsocketChatRoomListener(ChatRoom chatRoom, boolean admin, Collection<String> ignoredUsers, ChannelHandlerContext ctx) {
            this.chatRoom = chatRoom;
            this.admin = admin;
            this.ignoredUsers = ignoredUsers;
            this.ctx = ctx;
        }

        @Override
        public void messageReceived(ChatMessage message) {
            if (message.isForced() || !ignoredUsers.contains(message.getFrom())) {
                try {
                    Document document = XMLSerializeUtil.serializeChatRoomData(playerDao, chatRoom.getName(), Collections.singleton(message),
                            filterIgnoredUsers(ignoredUsers, chatRoom.getUsersInRoom(admin)));
                    writeXmlFrame(ctx, document);
                } catch (Exception e) {
                    ctx.close();
                }
            }
        }

        @Override
        public void userJoined(String userId) {
            if (!ignoredUsers.contains(userId)) {
                try {
                    Document document = XMLSerializeUtil.serializeChatRoomData(playerDao, chatRoom.getName(), Collections.emptySet(),
                            filterIgnoredUsers(ignoredUsers, chatRoom.getUsersInRoom(admin)));
                    writeXmlFrame(ctx, document);
                } catch (Exception e) {
                    ctx.close();
                }
            }
        }

        @Override
        public void userParted(String userId) {
            if (!ignoredUsers.contains(userId)) {
                try {
                    Document document = XMLSerializeUtil.serializeChatRoomData(playerDao, chatRoom.getName(), Collections.emptySet(),
                            filterIgnoredUsers(ignoredUsers, chatRoom.getUsersInRoom(admin)));
                    writeXmlFrame(ctx, document);
                } catch (Exception e) {
                    ctx.close();
                }
            }
        }

        @Override
        public void listenerPushedOut() {
            ctx.close();
        }
    }
}
