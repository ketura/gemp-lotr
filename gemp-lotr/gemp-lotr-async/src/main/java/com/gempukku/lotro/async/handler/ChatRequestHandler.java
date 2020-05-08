package com.gempukku.lotro.async.handler;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.async.HttpProcessingException;
import com.gempukku.lotro.async.ResponseWriter;
import com.gempukku.lotro.chat.MessagesAndUsers;
import com.gempukku.lotro.chat.ChatCommandErrorException;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.async.poll.ChatServerMediator;
import com.gempukku.lotro.async.poll.ChatCommunicationChannel;
import com.gempukku.lotro.game.Player;
import com.gempukku.polling.LongPollingResource;
import com.gempukku.polling.LongPollingSystem;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import org.apache.commons.lang.StringEscapeUtils;
import org.w3c.dom.Document;

import java.lang.reflect.Type;
import java.net.URLDecoder;
import java.util.*;

import static com.gempukku.lotro.async.XMLSerializeUtil.serializeChatRoomData;

public class ChatRequestHandler extends LotroServerRequestHandler implements UriRequestHandler {
    private ChatServerMediator chatServerMediator;
    private LongPollingSystem longPollingSystem;

    public ChatRequestHandler(Map<Type, Object> context, LongPollingSystem longPollingSystem) {
        super(context);
        this.chatServerMediator = (ChatServerMediator) context.get(ChatServerMediator.class);
        this.longPollingSystem = longPollingSystem;
    }

    @Override
    public void handleRequest(String uri, HttpRequest request, Map<Type, Object> context, ResponseWriter responseWriter, String remoteIp) throws Exception {
        if (uri.startsWith("/") && request.method() == HttpMethod.GET) {
            joinRoom(request, URLDecoder.decode(uri.substring(1)), responseWriter);
        } else if (uri.startsWith("/") && request.method() == HttpMethod.POST) {
            updateMessages(request, URLDecoder.decode(uri.substring(1)), responseWriter);
        } else {
            throw new HttpProcessingException(404);
        }
    }

    private void joinRoom(HttpRequest request, String room, ResponseWriter responseWriter) throws Exception {
        QueryStringDecoder queryDecoder = new QueryStringDecoder(request.uri());
        String participantId = getQueryParameterSafely(queryDecoder, "participantId");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        String playerName = resourceOwner.getName();

        try {
            final boolean admin = resourceOwner.getType().contains("a");
            MessagesAndUsers messagesAndUsers = chatServerMediator.joinUser(room, playerName, admin);
            if (messagesAndUsers == null)
                throw new HttpProcessingException(404);

            Document doc = serializeChatRoomData(_playerDao, room, messagesAndUsers.getChatMessages(), messagesAndUsers.getUsers());

            responseWriter.writeXmlResponse(doc);
        } catch (PrivateInformationException exp) {
            throw new HttpProcessingException(403);
        }
    }

    private void updateMessages(HttpRequest request, String room, ResponseWriter responseWriter) throws Exception {
        HttpPostRequestDecoder postDecoder = new HttpPostRequestDecoder(request);
        String participantId = getFormParameterSafely(postDecoder, "participantId");
        String message = getFormParameterSafely(postDecoder, "message");

        Player resourceOwner = getResourceOwnerSafely(request, participantId);
        String playerName = resourceOwner.getName();

        try {
            final boolean admin = resourceOwner.getType().contains("a");
            if (message != null && message.trim().length() > 0) {
                chatServerMediator.sendMessage(room, playerName, StringEscapeUtils.escapeHtml(message), admin);
                responseWriter.writeXmlResponse(null);
            } else {
                ChatCommunicationChannel pollableResource = chatServerMediator.getChatRoomCommunicationChannel(room, playerName);
                ChatUpdateLongPollingResource polledResource = new ChatUpdateLongPollingResource(chatServerMediator, room, playerName, admin, responseWriter);
                longPollingSystem.processLongPollingResource(polledResource, pollableResource);
            }
        } catch (SubscriptionExpiredException exp) {
            throw new HttpProcessingException(410);
        } catch (PrivateInformationException exp) {
            throw new HttpProcessingException(403);
        } catch (ChatCommandErrorException exp) {
            throw new HttpProcessingException(400);
        }
    }

    private class ChatUpdateLongPollingResource implements LongPollingResource {
        private ChatServerMediator chatRoom;
        private String room;
        private String playerId;
        private boolean admin;
        private ResponseWriter responseWriter;
        private boolean processed;

        private ChatUpdateLongPollingResource(ChatServerMediator chatRoom, String room, String playerId, boolean admin, ResponseWriter responseWriter) {
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
                    List<ChatMessage> chatMessages = chatRoom.getChatRoomCommunicationChannel(room, playerId).consumeMessages();

                    Collection<String> usersInRoom = chatServerMediator.getUsersInRoom(room, playerId, admin);

                    Document doc = serializeChatRoomData(_playerDao, room, chatMessages, usersInRoom);

                    responseWriter.writeXmlResponse(doc);
                } catch (SubscriptionExpiredException exp) {
                    responseWriter.writeError(410);
                } catch (Exception exp) {
                    responseWriter.writeError(500);
                }
                processed = true;
            }
        }

        @Override
        public void forciblyRemoved() {
            if (!processed) {
                responseWriter.writeError(409);
                processed = true;
            }
        }
    }
}
