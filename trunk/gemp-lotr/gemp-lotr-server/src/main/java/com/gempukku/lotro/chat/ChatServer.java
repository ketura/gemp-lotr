package com.gempukku.lotro.chat;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.PrivateInformationException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends AbstractServer {
    private Map<String, ChatRoomMediator> _chatRooms = new ConcurrentHashMap<String, ChatRoomMediator>();

    public ChatRoomMediator createChatRoom(String name, int secondsTimeoutPeriod) {
        ChatRoomMediator chatRoom = new ChatRoomMediator(name, secondsTimeoutPeriod);
        try {
            chatRoom.sendMessage("System", "Welcome to room: " + name, true);
        } catch (PrivateInformationException exp) {
            // Ignore, sent as admin
        }
        _chatRooms.put(name, chatRoom);
        return chatRoom;
    }

    public ChatRoomMediator createPrivateChatRoom(String name, Set<String> allowedUsers, int secondsTimeoutPeriod) {
        ChatRoomMediator chatRoom = new ChatRoomMediator(name, secondsTimeoutPeriod, allowedUsers);
        try {
            chatRoom.sendMessage("System", "Welcome to private room: " + name, true);
        } catch (PrivateInformationException exp) {
            // Ignore, sent as admin
        }
        _chatRooms.put(name, chatRoom);
        return chatRoom;
    }

    public void sendSystemMessageToAllChatRooms(String message) {
        try {
            for (ChatRoomMediator chatRoomMediator : _chatRooms.values())
                chatRoomMediator.sendMessage("System", message, true);
        } catch (PrivateInformationException exp) {
            // Ignore, sent as admin
        }
    }

    public ChatRoomMediator getChatRoom(String name) {
        return _chatRooms.get(name);
    }

    public void destroyChatRoom(String name) {
        _chatRooms.remove(name);
    }

    protected void cleanup() {
        for (ChatRoomMediator chatRoomMediator : _chatRooms.values())
            chatRoomMediator.cleanup();
    }
}
