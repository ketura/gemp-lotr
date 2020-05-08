package com.gempukku.lotro.chat;

import com.gempukku.lotro.PrivateInformationException;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.apache.log4j.Logger;

import java.util.stream.Collectors;

public class ChatRoom {
    private static final int MAX_MESSAGE_HISTORY_COUNT = 500;

    private Logger logger;

    private LinkedList<ChatMessage> lastMessages = new LinkedList<>();
    private Map<String, ChatRoomInfo> chatRoomListeners = new TreeMap<>(
            String::compareToIgnoreCase);
    private String roomName;
    private boolean allowIncognito;
    private Set<String> allowedPlayers;
    private String welcomeMessage;

    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Map<String, ChatCommandCallback> chatCommandCallbacks = new HashMap<String, ChatCommandCallback>();

    public ChatRoom(String roomName, boolean allowIncognito, Set<String> allowedPlayers, String welcomeMessage) {
        this.roomName = roomName;
        this.allowIncognito = allowIncognito;
        this.allowedPlayers = allowedPlayers;
        this.welcomeMessage = welcomeMessage;
        logger = Logger.getLogger("chat." + roomName);
    }

    public String getName() {
        return roomName;
    }

    public void addChatCommandCallback(String command, ChatCommandCallback callback) {
        chatCommandCallbacks.put(command.toLowerCase(), callback);
    }

    public void setUserIncognitoMode(String username, boolean incognito) {
        if (allowIncognito) {
            lock.writeLock().lock();
            try {
                final ChatRoomInfo chatRoomInfo = chatRoomListeners.get(username);
                if (chatRoomInfo != null)
                    chatRoomInfo.incognito = incognito;
            } finally {
                lock.writeLock().unlock();
            }
        }
    }

    public void postMessage(String from, String message, boolean admin) throws ChatCommandErrorException, PrivateInformationException {
        if (processIfKnownCommand(from, message, admin))
            return;

        logger.trace(from + ": " + message);
        lock.writeLock().lock();
        try {
            ChatMessage chatMessage = new ChatMessage(new Date(), from, message, admin);
            lastMessages.add(chatMessage);
            shrinkLastMessages();
            for (Map.Entry<String, ChatRoomInfo> listeners : chatRoomListeners.entrySet())
                listeners.getValue().chatRoomListener.messageReceived(chatMessage);
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void postToUser(String from, String to, String message) {
        lock.writeLock().lock();
        try {
            ChatMessage chatMessage = new ChatMessage(new Date(), from, message, false);
            final ChatRoomListener chatRoomListener = chatRoomListeners.get(to).chatRoomListener;
            if (chatRoomListener != null) {
                chatRoomListener.messageReceived(chatMessage);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public MessagesAndUsers joinChatRoom(String playerId, boolean admin, ChatRoomListener listener) throws PrivateInformationException {
        lock.writeLock().lock();
        try {
            if (!isAllowedPlayer(playerId, admin))
                throw new PrivateInformationException();

            boolean wasInRoom = false;
            ChatRoomInfo oldListener = chatRoomListeners.put(playerId, new ChatRoomInfo(listener, false));
            if (oldListener != null) {
                wasInRoom = true;
                oldListener.chatRoomListener.listenerPushedOut();
            }

            if (!wasInRoom) {
                for (Map.Entry<String, ChatRoomInfo> listeners : chatRoomListeners.entrySet())
                    listeners.getValue().chatRoomListener.userJoined(playerId);
            }

            LinkedList<ChatMessage> messages = new LinkedList<>(lastMessages);
            if (welcomeMessage != null)
                messages.addLast(new ChatMessage(new Date(), "System", welcomeMessage, true));

            return new MessagesAndUsers(messages, getUsersInRoom(admin));
        } finally {
            lock.writeLock().unlock();
        }
    }

    public void partChatRoom(String playerId) {
        lock.writeLock().lock();
        try {
            boolean wasInRoom = (chatRoomListeners.remove(playerId) != null);
            if (wasInRoom) {
                for (Map.Entry<String, ChatRoomInfo> listeners : chatRoomListeners.entrySet())
                    listeners.getValue().chatRoomListener.userParted(playerId);
            }
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Collection<String> getUsersInRoom(boolean includeIncognito) {
        lock.readLock().lock();
        try {
            if (includeIncognito)
                return new ArrayList<>(chatRoomListeners.keySet());
            else {
                return chatRoomListeners.entrySet().stream().filter(
                        entry -> !entry.getValue().incognito).map(Map.Entry::getKey).collect(Collectors.toList());
            }
        } finally {
            lock.readLock().unlock();
        }
    }

    public void destroy() {
        lock.writeLock().lock();
        try {

        } finally {
            lock.writeLock().unlock();
        }
    }

    private void shrinkLastMessages() {
        while (lastMessages.size() > MAX_MESSAGE_HISTORY_COUNT) {
            lastMessages.removeFirst();
        }
    }

    private boolean processIfKnownCommand(String playerId, String message, boolean admin) throws ChatCommandErrorException {
        if (message.startsWith("/")) {
            // Maybe it's a known command
            String commandString = message.substring(1);
            int spaceIndex = commandString.indexOf(" ");
            String commandName;
            String commandParameters = "";
            if (spaceIndex > -1) {
                commandName = commandString.substring(0, spaceIndex);
                commandParameters = commandString.substring(spaceIndex + 1);
            } else {
                commandName = commandString;
            }
            final ChatCommandCallback callbackForCommand = chatCommandCallbacks.get(commandName.toLowerCase());
            if (callbackForCommand != null) {
                callbackForCommand.commandReceived(playerId, commandParameters, admin);
                return true;
            }
        }
        return false;
    }

    private boolean isAllowedPlayer(String from, boolean admin) {
        return admin || allowedPlayers == null || allowedPlayers.contains(from);
    }

    private class ChatRoomInfo {
        private ChatRoomListener chatRoomListener;
        private boolean incognito;

        public ChatRoomInfo(ChatRoomListener chatRoomListener, boolean incognito) {
            this.chatRoomListener = chatRoomListener;
            this.incognito = incognito;
        }
    }
}
