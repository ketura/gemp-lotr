package com.gempukku.lotro.async.poll;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomListener;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.WaitingRequest;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ChatCommunicationChannel implements ChatRoomListener, LongPollableResource {
    private List<ChatMessage> messages = new LinkedList<ChatMessage>();
    private long lastConsumed = System.currentTimeMillis();
    private volatile WaitingRequest waitingRequest;
    private Set<String> ignoredUsers;

    public ChatCommunicationChannel(Set<String> ignoredUsers) {
        this.ignoredUsers = ignoredUsers;
    }

    @Override
    public synchronized void deregisterRequest(WaitingRequest waitingRequest) {
        this.waitingRequest = null;
    }

    @Override
    public synchronized boolean registerRequest(WaitingRequest waitingRequest) {
        if (messages.size() > 0)
            return true;

        this.waitingRequest = waitingRequest;
        return false;
    }

    @Override
    public synchronized void messageReceived(ChatMessage message) {
        if (message.isForced() || !ignoredUsers.contains(message.getFrom())) {
            messages.add(message);
            updateWaitingRequest();
        }
    }

    @Override
    public synchronized void userJoined(String userId) {
        if (!ignoredUsers.contains(userId)) {
            updateWaitingRequest();
        }
    }

    @Override
    public synchronized void userParted(String userId) {
        if (!ignoredUsers.contains(userId)) {
            updateWaitingRequest();
        }
    }

    private void updateWaitingRequest() {
        if (waitingRequest != null) {
            waitingRequest.processRequest();
            waitingRequest = null;
        }
    }

    @Override
    public synchronized void listenerPushedOut() {
        forcedRemoval();
    }

    public synchronized void forcedRemoval() {
        if (waitingRequest != null) {
            waitingRequest.forciblyRemoved();
            waitingRequest = null;
        }
    }

    public synchronized List<ChatMessage> consumeMessages() {
        updateLastAccess();
        List<ChatMessage> messages = this.messages;
        this.messages = new LinkedList<ChatMessage>();
        return messages;
    }

    private synchronized void updateLastAccess() {
        lastConsumed = System.currentTimeMillis();
    }

    public synchronized long getLastAccess() {
        return lastConsumed;
    }
}
