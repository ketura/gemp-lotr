package com.gempukku.lotro.chat;

import java.util.*;

public class ChatRoom {
    private int _maxMessageHistoryCount = 500;
    private LinkedList<ChatMessage> _lastMessages = new LinkedList<ChatMessage>();
    private Map<String, ChatRoomListener> _chatRoomListeners = new TreeMap<String, ChatRoomListener>(
            new Comparator<String>() {
                @Override
                public int compare(String o1, String o2) {
                    return o1.compareToIgnoreCase(o2);
                }
            });
    private boolean _muteJoinPartMessages;

    public ChatRoom(boolean muteJoinPartMessages) {
        _muteJoinPartMessages = muteJoinPartMessages;
    }

    public void postMessage(String from, String message, boolean addToHistory, boolean fromAdmin) {
        ChatMessage chatMessage = new ChatMessage(new Date(), from, message, fromAdmin);
        if (addToHistory) {
            _lastMessages.add(chatMessage);
            shrinkLastMessages();
        }
        for (Map.Entry<String, ChatRoomListener> listeners : _chatRoomListeners.entrySet())
            listeners.getValue().messageReceived(chatMessage);
    }

    public void postToUser(String from, String to, String message) {
        ChatMessage chatMessage = new ChatMessage(new Date(), from, message, false);
        final ChatRoomListener chatRoomListener = _chatRoomListeners.get(to);
        if (chatRoomListener != null) {
            chatRoomListener.messageReceived(chatMessage);
        }
    }

    public void joinChatRoom(String playerId, ChatRoomListener listener) {
        boolean wasInRoom = _chatRoomListeners.containsKey(playerId);
        _chatRoomListeners.put(playerId, listener);
        for (ChatMessage lastMessage : _lastMessages)
            listener.messageReceived(lastMessage);
        if (!wasInRoom && !_muteJoinPartMessages)
            postMessage("System", playerId + " joined the room", true, false);
    }

    public void partChatRoom(String playerId) {
        boolean wasInRoom = (_chatRoomListeners.remove(playerId) != null);
        if (wasInRoom && !_muteJoinPartMessages)
            postMessage("System", playerId + " left the room", true, false);
    }

    public Collection<String> getUsersInRoom() {
        return new ArrayList<String>(_chatRoomListeners.keySet());
    }

    private void shrinkLastMessages() {
        while (_lastMessages.size() > _maxMessageHistoryCount) {
            _lastMessages.removeFirst();
        }
    }
}
