package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.chat.ChatCommandErrorException;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomMediator;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.game.ChatCommunicationChannel;
import com.gempukku.lotro.game.Player;
import com.gempukku.polling.LongPollingResource;
import com.gempukku.polling.LongPollingSystem;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.*;

public class ChatRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private ChatServer _chatServer;
    private LongPollingSystem longPollingSystem;

    public ChatRequestHandler(Map<Type, Object> context, LongPollingSystem longPollingSystem) {
        super(context);
        _chatServer = extractObject(context, ChatServer.class);
        this.longPollingSystem = longPollingSystem;
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.startsWith("/") && request.getMethod() == HttpMethod.GET) {
            getMessages(request, URLDecoder.decode(uri.substring(1)), responseWriter);
        } else if (uri.startsWith("/") && request.getMethod() == HttpMethod.POST) {
            postMessages(request, URLDecoder.decode(uri.substring(1)), responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    private void postMessages(HttpRequest request, String room, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        try {
            String participantId = getFormParameterSafely(postDecoder, "participantId");
            String message = getFormParameterSafely(postDecoder, "message");

            Player resourceOwner = getResourceOwnerSafely(request, participantId);

            ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
            if (chatRoom == null)
                throw new HttpProcessingException(404);

            try {
                final boolean admin = resourceOwner.getType().contains("a");
                if (message != null && message.trim().length() > 0) {
                    chatRoom.sendMessage(resourceOwner.getName(), StringEscapeUtils.escapeHtml(message), admin);
                    responseWriter.writeXmlResponse(null);
                } else {
                    ChatCommunicationChannel pollableResource = chatRoom.getChatRoomListener(resourceOwner.getName());
                    ChatUpdateLongPollingResource polledResource = new ChatUpdateLongPollingResource(chatRoom, room, resourceOwner.getName(), admin, responseWriter);
                    longPollingSystem.processLongPollingResource(polledResource, pollableResource);
                }
            } catch (SubscriptionExpiredException exp) {
                throw new HttpProcessingException(410);
            } catch (PrivateInformationException exp) {
                throw new HttpProcessingException(403);
            } catch (ChatCommandErrorException exp) {
                throw new HttpProcessingException(400);
            }
        } finally {
            postDecoder.destroy();
        }
    }

    private class ChatUpdateLongPollingResource implements LongPollingResource {
        private ChatRoomMediator chatRoom;
        private String room;
        private String playerId;
        private boolean admin;
        private ResponseWriter responseWriter;
        private boolean processed;

        private ChatUpdateLongPollingResource(ChatRoomMediator chatRoom, String room, String playerId, boolean admin, ResponseWriter responseWriter) {
            this.chatRoom = chatRoom;
            this.room = room;
            this.playerId = playerId;
            this.admin = admin;
            this.responseWriter = responseWriter;
        }

        @Override
        public synchronized boolean wasProcessed() {
            return processed;
        }

        @Override
        public synchronized void processIfNotProcessed() {
            if (!processed) {
                try {
                    List<ChatMessage> chatMessages = chatRoom.getChatRoomListener(playerId).consumeMessages();

                    Collection<String> usersInRoom = chatRoom.getUsersInRoom(admin);

                    DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
                    DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

                    Document doc = documentBuilder.newDocument();

                    serializeChatRoomData(room, chatMessages, usersInRoom, doc);

                    responseWriter.writeXmlResponse(doc);
                } catch (SubscriptionExpiredException exp) {
                    responseWriter.writeError(410);
                } catch (Exception exp) {
                    responseWriter.writeError(500);
                }
                processed = true;
            }
        }
    }

    private void getMessages(HttpRequest request, String room, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.getUri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);

        ChatRoomMediator chatRoom = _chatServer.getChatRoom(room);
        if (chatRoom == null)
            throw new HttpProcessingException(404);
        try {
            final boolean admin = resourceOwner.getType().contains("a");
            List<ChatMessage> chatMessages = chatRoom.joinUser(resourceOwner.getName(), admin);
            Collection<String> usersInRoom = chatRoom.getUsersInRoom(admin);

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Document doc = documentBuilder.newDocument();

            serializeChatRoomData(room, chatMessages, usersInRoom, doc);

            responseWriter.writeXmlResponse(doc);
        } catch (PrivateInformationException exp) {
            throw new HttpProcessingException(403);
        }
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

    private class CaseInsensitiveStringComparator implements Comparator<String> {
        @Override
        public int compare(String o1, String o2) {
            return o1.toLowerCase().compareTo(o2.toLowerCase());
        }
    }

    private String formatPlayerNameForChatList(String userInRoom) {
        final Player player = _playerDao.getPlayer(userInRoom);
        if (player != null) {
            final String playerType = player.getType();
            if (playerType.contains("a"))
                return "* "+userInRoom;
            else if (playerType.contains("l"))
                return "+ "+userInRoom;
        }
        return userInRoom;
    }
}
