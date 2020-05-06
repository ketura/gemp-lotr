package com.gempukku.lotro.async;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.async.handler.ChatRequestHandler;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.PlayerDAO;
import com.gempukku.lotro.game.ChatCommunicationChannel;
import com.gempukku.lotro.game.Player;
import com.gempukku.lotro.service.LoggedUserHolder;
import com.gempukku.polling.WaitingRequest;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.cookie.Cookie;
import io.netty.handler.codec.http.cookie.ServerCookieDecoder;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static io.netty.handler.codec.http.HttpHeaderNames.COOKIE;

public class GempukkuWebsocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    private final ChatServer chatServer;
    private String websocketPath;
    private Map<Type, Object> context;

    private String websocketType;
    private String websocketIdentifier;
    private String playerId;
    private boolean admin;

    private final LoggedUserHolder loggedUserHolder;
    private final PlayerDAO playerDao;

    public GempukkuWebsocketHandler(String websocketPath, Map<Type, Object> context) {
        this.websocketPath = websocketPath;
        this.context = context;
        loggedUserHolder = (LoggedUserHolder) context.get(LoggedUserHolder.class);
        playerDao = (PlayerDAO) context.get(PlayerDAO.class);
        chatServer = (ChatServer) context.get(ChatServer.class);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof WebSocketServerProtocolHandler.HandshakeComplete) {
            System.out.println("Hanshake completed");
            WebSocketServerProtocolHandler.HandshakeComplete handshake = (WebSocketServerProtocolHandler.HandshakeComplete) evt;
            String uri = handshake.requestUri();
            HttpHeaders headers = handshake.requestHeaders();

            processWebsocketConnected(ctx, uri, headers);
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        if (websocketType!= null && websocketType.equals("chat")) {
            ChatRoomMediator chatRoom = chatServer.getChatRoom(websocketIdentifier);
            if (chatRoom != null) {
                chatRoom.sendMessage(playerId, msg.text(), admin);
            } else {
                ctx.close();
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

            ChatRoomMediator chatRoom = chatServer.getChatRoom(room);
            if (chatRoom == null)
                throw new HttpProcessingException(404);
            try {
                websocketType = "chat";
                websocketIdentifier = room;

                admin = resourceOwner.getType().contains("a");
                playerId = resourceOwner.getName();
                List<ChatMessage> chatMessages = chatRoom.joinUser(playerId, false, admin);
                Collection<String> usersInRoom = chatRoom.getUsersInRoom(admin);

                DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                Document doc = documentBuilder.newDocument();

                serializeChatRoomData(room, chatMessages, usersInRoom, doc);

                writeXmlFrame(ctx, doc);

                ChatCommunicationChannel chatRoomListener = chatRoom.getChatRoomListener(playerId);
                chatRoomListener.registerRequest(
                        new WaitingRequest() {
                            @Override
                            public void processRequest() {
                                try {
                                    List<ChatMessage> chatMessages = chatRoom.getChatRoomListener(playerId).consumeMessages();

                                    Collection<String> usersInRoom = chatRoom.getUsersInRoom(admin);

                                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                                    Document doc = documentBuilder.newDocument();

                                    serializeChatRoomData(room, chatMessages, usersInRoom, doc);

                                    writeXmlFrame(ctx, doc);
                                } catch (Exception exp) {
                                    ctx.close();
                                }
                            }

                            @Override
                            public boolean isOneShot() {
                                return false;
                            }

                            @Override
                            public void forciblyRemoved() {
                                ctx.close();
                            }
                        });
            } catch (PrivateInformationException | SubscriptionExpiredException exp) {
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


    private void serializeChatRoomData(String room, List<ChatMessage> chatMessages, Collection<String> usersInRoom, Document doc) {
        Element chatElem = doc.createElement("chat");
        chatElem.setAttribute("roomName", room);
        doc.appendChild(chatElem);

        for (ChatMessage chatMessage : chatMessages) {
            Element message = doc.createElement("message");
            message.setAttribute("from", chatMessage.getFrom());
            message.setAttribute("date", String.valueOf(chatMessage.getWhen().getTime()));
            message.appendChild(doc.createTextNode(chatMessage.getMessage()));
            chatElem.appendChild(message);
        }

        Set<String> users = new TreeSet<String>(new CaseInsensitiveStringComparator());
        for (String userInRoom : usersInRoom)
            users.add(formatPlayerNameForChatList(userInRoom));

        for (String userInRoom : users) {
            Element user = doc.createElement("user");
            user.appendChild(doc.createTextNode(userInRoom));
            chatElem.appendChild(user);
        }
    }

    private String formatPlayerNameForChatList(String userInRoom) {
        final Player player = playerDao.getPlayer(userInRoom);
        if (player != null) {
            final String playerType = player.getType();
            if (playerType.contains("a"))
                return "* " + userInRoom;
            else if (playerType.contains("l"))
                return "+ " + userInRoom;
        }
        return userInRoom;
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

    private boolean isTest() {
        return Boolean.valueOf(System.getProperty("test"));
    }

    private class CaseInsensitiveStringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }
}
