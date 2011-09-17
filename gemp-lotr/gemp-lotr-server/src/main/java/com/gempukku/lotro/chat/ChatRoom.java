package com.gempukku.lotro.chat;

import java.util.*;

public class ChatRoom {
    private int _maxMessageCount = 10;
    private LinkedList<ChatMessage> _lastMessages = new LinkedList<ChatMessage>();
    private Map<String, ChatRoomListener> _chatRoomListeners = new HashMap<String, ChatRoomListener>();

    public ChatRoom() {
    }

    public void postMessage(String from, String message) {
        ChatMessage chatMessage = new ChatMessage(new Date(), from, message);
        _lastMessages.add(chatMessage);
        shrinkLastMessages();
        for (Map.Entry<String, ChatRoomListener> listeners : _chatRoomListeners.entrySet())
            if (!listeners.getKey().equals(from))
                listeners.getValue().messageReceived(chatMessage);
    }

    public void joinChatRoom(String playerId, ChatRoomListener listener) {
        _chatRoomListeners.put(playerId, listener);
        for (ChatMessage lastMessage : _lastMessages)
            listener.messageReceived(lastMessage);
    }

    public void partChatRoom(String playerId) {
        _chatRoomListeners.remove(playerId);
    }

    public Set<String> getUsersInRoom() {
        return new TreeSet<String>(_chatRoomListeners.keySet());
    }

    private void shrinkLastMessages() {
        while (_lastMessages.size() > _maxMessageCount) {
            _lastMessages.removeFirst();
        }
    }
}
