package com.gempukku.lotro.chat;

import com.gempukku.lotro.PrivateInformationException;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ChatServer {
    private Map<String, ChatRoom> chatRooms = new ConcurrentHashMap<>();

    public ChatRoom createChatRoom(String name, boolean allowIncognito, String welcomeMessage) {
        ChatRoom chatRoom = new ChatRoom(name, allowIncognito, null, welcomeMessage);
        try {
            chatRoom.postMessage("System", "Welcome to room: " + name, true);
        } catch (PrivateInformationException e) {
            // Ignore, sent as admin
        } catch (ChatCommandErrorException e) {
            // Ignore, no command
        }
        chatRooms.put(name, chatRoom);
        return chatRoom;
    }

    public ChatRoom createPrivateChatRoom(String name, Set<String> allowedUsers) {
        ChatRoom chatRoom = new ChatRoom(name, false, allowedUsers, null);
        try {
            chatRoom.postMessage("System", "Welcome to private room: " + name, true);
        } catch (PrivateInformationException e) {
            // Ignore, sent as admin
        } catch (ChatCommandErrorException e) {
            // Ignore, no command
        }
        chatRooms.put(name, chatRoom);
        return chatRoom;
    }

    public void sendSystemMessageToAllChatRooms(String message) {
        try {
            for (ChatRoom chatRoom : chatRooms.values())
                chatRoom.postMessage("System", message, true);
        } catch (PrivateInformationException e) {
            // Ignore, sent as admin
        } catch (ChatCommandErrorException e) {
            // Ignore, no command
        }
    }

    public ChatRoom getChatRoom(String name) {
        return chatRooms.get(name);
    }

    public void destroyChatRoom(String name) {
        ChatRoom chatRoom = chatRooms.remove(name);
        chatRoom.destroy();
    }
}
