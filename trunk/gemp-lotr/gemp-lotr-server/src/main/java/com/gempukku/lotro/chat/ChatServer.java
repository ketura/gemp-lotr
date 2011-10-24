package com.gempukku.lotro.chat;

import com.gempukku.lotro.AbstractServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer extends AbstractServer {
    private Map<String, ChatRoomMediator> _chatRooms = new ConcurrentHashMap<String, ChatRoomMediator>();

    public ChatRoomMediator createChatRoom(String name) {
        ChatRoomMediator chatRoom = new ChatRoomMediator();
        chatRoom.sendMessage("System", "Welcome to room: " + name);
        _chatRooms.put(name, chatRoom);
        return chatRoom;
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
