package com.gempukku.lotro.async.poll;

import com.gempukku.lotro.AbstractServer;
import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.chat.ChatCommandErrorException;
import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoom;
import com.gempukku.lotro.chat.ChatServer;
import com.gempukku.lotro.db.IgnoreDAO;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatServerMediator extends AbstractServer {
    private ChatServer chatServer;
    private IgnoreDAO ignoreDAO;
    private final int channelInactivityTimeoutPeriod;

    private Map<String, Map<String, ChatCommunicationChannel>> communicationChannels = new HashMap();

    private ReadWriteLock lock = new ReentrantReadWriteLock();

    public ChatServerMediator(ChatServer chatServer, IgnoreDAO ignoreDAO, int secondsTimeoutPeriod) {
        this.chatServer = chatServer;
        this.ignoreDAO = ignoreDAO;
        this.channelInactivityTimeoutPeriod = 1000 * secondsTimeoutPeriod;
    }

    public MessagesAndUsers joinUser(String room, String playerId, boolean admin) throws PrivateInformationException {
        lock.writeLock().lock();
        try {
            ChatCommunicationChannel chatCommunicationChannel = new ChatCommunicationChannel(ignoreDAO.getIgnoredUsers(playerId));
            ChatRoom chatRoom = chatServer.getChatRoom(room);
            if (chatRoom == null)
                return null;

            Collection<String> usersInRoom = chatRoom.joinChatRoom(playerId, admin, chatCommunicationChannel);
            communicationChannels.putIfAbsent(room, new HashMap<>()).put(playerId, chatCommunicationChannel);

            List<ChatMessage> messages = chatCommunicationChannel.consumeMessages();
            return new MessagesAndUsers(messages, usersInRoom);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public ChatCommunicationChannel getChatRoomCommunicationChannel(String room, String playerId) throws SubscriptionExpiredException {
        lock.readLock().lock();
        try {
            Map<String, ChatCommunicationChannel> roomChannels = communicationChannels.get(room);
            if (roomChannels == null)
                throw new SubscriptionExpiredException();

            ChatCommunicationChannel chatCommunicationChannel = roomChannels.get(playerId);
            if (chatCommunicationChannel == null)
                throw new SubscriptionExpiredException();

            return chatCommunicationChannel;
        } finally {
            lock.readLock().unlock();
        }
    }

    public void sendMessage(String room, String playerId, String message, boolean admin) throws ChatCommandErrorException, PrivateInformationException, SubscriptionExpiredException {
        lock.writeLock().lock();
        try {
            ChatRoom chatRoom = chatServer.getChatRoom(room);
            if (chatRoom == null)
                throw new SubscriptionExpiredException();
            chatRoom.postMessage(playerId, message, admin);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Collection<String> getUsersInRoom(String room, String forPlayer, boolean includeIncognito) throws SubscriptionExpiredException {
        lock.readLock().lock();
        try {
            ChatRoom chatRoom = chatServer.getChatRoom(room);
            if (chatRoom == null)
                throw new SubscriptionExpiredException();

            Set<String> ignoredUsers = ignoreDAO.getIgnoredUsers(forPlayer);
            List<String> result = new LinkedList<>();
            for (String playerName : chatRoom.getUsersInRoom(includeIncognito)) {
                if (!ignoredUsers.contains(playerName))
                    result.add(playerName);
            }
            return result;
        } finally {
            lock.readLock().unlock();
        }
    }

    @Override
    public void cleanup() {
        lock.writeLock().lock();
        try {
            long currentTime = System.currentTimeMillis();
            Map<String, Map<String, ChatCommunicationChannel>> serverCommunicationChannelsCopy = new HashMap<>(communicationChannels);
            for (Map.Entry<String, Map<String, ChatCommunicationChannel>> roomCommunicationChannelsCopy : serverCommunicationChannelsCopy.entrySet()) {
                String roomName = roomCommunicationChannelsCopy.getKey();
                ChatRoom chatRoom = chatServer.getChatRoom(roomName);
                if (chatRoom == null) {
                    // Chat room is gone - kick everyone still lingering out
                    for (ChatCommunicationChannel channel : roomCommunicationChannelsCopy.getValue().values())
                        channel.forcedRemoval();
                    communicationChannels.remove(roomName);
                } else {
                    Map<String, ChatCommunicationChannel> realCommunicationChannels = communicationChannels.get(roomName);
                    // Check if they are not responding too long, and if so - kick them out
                    Map<String, ChatCommunicationChannel> communicationChannelsCopy = new HashMap<>(roomCommunicationChannelsCopy.getValue());
                    for (Map.Entry<String, ChatCommunicationChannel> playerChannel : communicationChannelsCopy.entrySet()) {
                        if (playerChannel.getValue().getLastAccess() + channelInactivityTimeoutPeriod < currentTime) {
                            String playerName = playerChannel.getKey();
                            chatRoom.partChatRoom(playerName);
                            realCommunicationChannels.remove(playerName);
                        }
                    }
                }
            }
        } finally {
            lock.writeLock().unlock();
        }
    }
}
