package com.gempukku.lotro.game;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomListener;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.WaitingRequest;

import java.util.LinkedList;
import java.util.List;

public class ChatCommunicationChannel implements ChatRoomListener, LongPollableResource {
    private List<ChatMessage> _messages = new LinkedList<ChatMessage>();
    private long _lastConsumed = System.currentTimeMillis();
    private WaitingRequest _waitingRequest;

    @Override
    public synchronized void deregisterRequest(WaitingRequest waitingRequest) {
        _waitingRequest = null;
    }

    @Override
    public synchronized boolean registerRequest(WaitingRequest waitingRequest) {
        if (_messages.size()>0)
            return true;

        _waitingRequest = waitingRequest;
        return false;
    }

    @Override
    public synchronized void messageReceived(ChatMessage message) {
        _messages.add(message);
        if (_waitingRequest != null) {
            _waitingRequest.processRequest();
            _waitingRequest = null;
        }
    }

    public synchronized List<ChatMessage> consumeMessages() {
        updateLastAccess();
        List<ChatMessage> messages = _messages;
        _messages = new LinkedList<ChatMessage>();
        return messages;
    }

    public synchronized boolean hasMessages() {
        updateLastAccess();
        return _messages.size() > 0;
    }

    private synchronized void updateLastAccess() {
        _lastConsumed = System.currentTimeMillis();
    }

    public synchronized long getLastAccessed() {
        return _lastConsumed;
    }
}
