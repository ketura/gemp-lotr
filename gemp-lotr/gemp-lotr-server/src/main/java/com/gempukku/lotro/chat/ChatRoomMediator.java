package com.gempukku.lotro.chat;

import com.gempukku.lotro.game.GatheringChatRoomListener;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import java.util.*;

public class ChatRoomMediator {
    private ChatRoom _chatRoom = new ChatRoom();

    private Map<String, GatheringChatRoomListener> _listeners = new HashMap<String, GatheringChatRoomListener>();

    private int _channelInactivityTimeoutPeriod = 1000 * 10; // 10 seconds
    private Set<String> _allowedPlayers;

    public ChatRoomMediator(int secondsTimeoutPeriod) {
        this(secondsTimeoutPeriod, null);
    }

    public ChatRoomMediator(int secondsTimeoutPeriod, Set<String> allowedPlayers) {
        _allowedPlayers = allowedPlayers;
        _channelInactivityTimeoutPeriod = 1000 * secondsTimeoutPeriod;
    }

    public synchronized List<ChatMessage> joinUser(String playerId) {
        GatheringChatRoomListener value = new GatheringChatRoomListener();
        _listeners.put(playerId, value);
        _chatRoom.joinChatRoom(playerId, value);
        return value.consumeMessages();
    }

    public synchronized List<ChatMessage> getPendingMessages(String playerId) {
        GatheringChatRoomListener gatheringChatRoomListener = _listeners.get(playerId);
        if (gatheringChatRoomListener == null)
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        return gatheringChatRoomListener.consumeMessages();
    }

    public synchronized void partUser(String playerId) {
        _chatRoom.partChatRoom(playerId);
        _listeners.remove(playerId);
    }

    public synchronized void sendMessage(String playerId, String message, boolean admin) {
        if (!admin && _allowedPlayers != null && !_allowedPlayers.contains(playerId))
            throw new WebApplicationException(Response.Status.FORBIDDEN);

        _chatRoom.postMessage(playerId, message);
    }

    public synchronized void cleanup() {
        long currentTime = System.currentTimeMillis();
        Map<String, GatheringChatRoomListener> copy = new HashMap<String, GatheringChatRoomListener>(_listeners);
        for (Map.Entry<String, GatheringChatRoomListener> playerListener : copy.entrySet()) {
            String playerId = playerListener.getKey();
            GatheringChatRoomListener listener = playerListener.getValue();
            if (currentTime > listener.getLastConsumed().getTime() + _channelInactivityTimeoutPeriod) {
                _chatRoom.partChatRoom(playerId);
                _listeners.remove(playerId);
            }
        }
    }

    public synchronized Collection<String> getUsersInRoom() {
        return _chatRoom.getUsersInRoom();
    }
}
