package com.gempukku.lotro.chat;

import com.gempukku.lotro.PrivateInformationException;
import com.gempukku.lotro.SubscriptionExpiredException;
import com.gempukku.lotro.game.ChatCommunicationChannel;
import org.apache.log4j.Logger;

import java.util.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ChatRoomMediator {
    private Logger _logger;
    private ChatRoom _chatRoom;

    private Map<String, ChatCommunicationChannel> _listeners = new HashMap<String, ChatCommunicationChannel>();

    private final int _channelInactivityTimeoutPeriod;
    private Set<String> _allowedPlayers;

    private ReadWriteLock _lock = new ReentrantReadWriteLock();

    private Map<String, ChatCommandCallback> _chatCommandCallbacks = new HashMap<String, ChatCommandCallback>();

    public ChatRoomMediator(String roomName, boolean muteJoinPartMessages, int secondsTimeoutPeriod) {
        this(roomName, muteJoinPartMessages, secondsTimeoutPeriod, null);
    }

    public ChatRoomMediator(String roomName, boolean muteJoinPartMessages, int secondsTimeoutPeriod, Set<String> allowedPlayers) {
        _logger = Logger.getLogger("chat."+roomName);
        _allowedPlayers = allowedPlayers;
        _channelInactivityTimeoutPeriod = 1000 * secondsTimeoutPeriod;
        _chatRoom = new ChatRoom(muteJoinPartMessages);
    }

    public void addChatCommandCallback(String command, ChatCommandCallback callback) {
        _chatCommandCallbacks.put(command.toLowerCase(), callback);
    }

    public List<ChatMessage> joinUser(String playerId, boolean admin) throws PrivateInformationException {
        _lock.writeLock().lock();
        try {
            if (!admin && _allowedPlayers != null && !_allowedPlayers.contains(playerId))
                throw new PrivateInformationException();

            ChatCommunicationChannel value = new ChatCommunicationChannel();
            _listeners.put(playerId, value);
            _chatRoom.joinChatRoom(playerId, value);
            return value.consumeMessages();
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public ChatCommunicationChannel getChatRoomListener(String playerId) throws SubscriptionExpiredException {
        _lock.readLock().lock();
        try {
            ChatCommunicationChannel gatheringChatRoomListener = _listeners.get(playerId);
            if (gatheringChatRoomListener == null)
                throw new SubscriptionExpiredException();
            return gatheringChatRoomListener;
        } finally {
            _lock.readLock().unlock();
        }
    }

    public void partUser(String playerId) {
        _lock.writeLock().lock();
        try {
            _chatRoom.partChatRoom(playerId);
            _listeners.remove(playerId);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public void sendMessage(String playerId, String message, boolean admin) throws PrivateInformationException, ChatCommandErrorException {
        if (processIfKnownCommand(playerId, message, admin))
            return;

        _lock.writeLock().lock();
        try {
            if (!admin && _allowedPlayers != null && !_allowedPlayers.contains(playerId))
                throw new PrivateInformationException();

            _logger.trace(playerId+": "+message);
            _chatRoom.postMessage(playerId, message);
        } finally {
            _lock.writeLock().unlock();
        }
    }

    private boolean processIfKnownCommand(String playerId, String message, boolean admin) throws ChatCommandErrorException {
        if (message.startsWith("/")) {
            // Maybe it's a known command
            String commandString = message.substring(1);
            int spaceIndex = commandString.indexOf(" ");
            String commandName;
            String commandParameters="";
            if (spaceIndex>-1) {
                commandName = commandString.substring(0, spaceIndex);
                commandParameters = commandString.substring(spaceIndex+1);
            } else {
                commandName = commandString;
            }
            final ChatCommandCallback callbackForCommand = _chatCommandCallbacks.get(commandName.toLowerCase());
            if (callbackForCommand != null) {
                callbackForCommand.commandReceived(playerId, commandParameters, admin);
                return true;
            }
        }
        return false;
    }

    public void cleanup() {
        _lock.writeLock().lock();
        try {
            long currentTime = System.currentTimeMillis();
            Map<String, ChatCommunicationChannel> copy = new HashMap<String, ChatCommunicationChannel>(_listeners);
            for (Map.Entry<String, ChatCommunicationChannel> playerListener : copy.entrySet()) {
                String playerId = playerListener.getKey();
                ChatCommunicationChannel listener = playerListener.getValue();
                if (currentTime > listener.getLastAccessed() + _channelInactivityTimeoutPeriod) {
                    _chatRoom.partChatRoom(playerId);
                    _listeners.remove(playerId);
                }
            }
        } finally {
            _lock.writeLock().unlock();
        }
    }

    public Collection<String> getUsersInRoom() {
        _lock.readLock().lock();
        try {
            return _chatRoom.getUsersInRoom();
        } finally {
            _lock.readLock().unlock();
        }
    }
}
