package com.gempukku.lotro.chat;

import java.util.*;

public class ChatRoom {
    private int _maxMessageHistoryCount = 150;
    private LinkedList<ChatMessage> _lastMessages = new LinkedList<ChatMessage>();
    private Map<String, ChatRoomListener> _chatRoomListeners = new HashMap<String, ChatRoomListener>();

    public ChatRoom() {
    }

    private void postMessage(String from, String message, boolean addToHistory) {
        ChatMessage chatMessage = new ChatMessage(new Date(), from, message);
        if (addToHistory) {
            _lastMessages.add(chatMessage);
            shrinkLastMessages();
        }
        for (Map.Entry<String, ChatRoomListener> listeners : _chatRoomListeners.entrySet())
            if (!listeners.getKey().equals(from))
                listeners.getValue().messageReceived(chatMessage);
    }

    public void postMessage(String from, String message) {
        postMessage(from, message, true);
    }

    public void joinChatRoom(String playerId, ChatRoomListener listener) {
        boolean wasInRoom = _chatRoomListeners.containsKey(playerId);
        _chatRoomListeners.put(playerId, listener);
        for (ChatMessage lastMessage : _lastMessages)
            listener.messageReceived(lastMessage);
        if (!wasInRoom)
            postMessage("System", playerId + " joined the room", false);
    }

    public void partChatRoom(String playerId) {
        boolean wasInRoom = (_chatRoomListeners.remove(playerId) != null);
        if (wasInRoom)
            postMessage("System", playerId + " left the room", false);
    }

    public Set<String> getUsersInRoom() {
        return new TreeSet<String>(_chatRoomListeners.keySet());
    }

    private void shrinkLastMessages() {
        while (_lastMessages.size() > _maxMessageHistoryCount) {
            _lastMessages.removeFirst();
        }
    }
}
