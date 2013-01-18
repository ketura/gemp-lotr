package com.gempukku.lotro.game;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomListener;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.LongPollingResource;

import java.util.LinkedList;
import java.util.List;

public class ChatCommunicationChannel implements ChatRoomListener, LongPollableResource {
    private List<ChatMessage> _messages = new LinkedList<ChatMessage>();
    private long _lastConsumed = System.currentTimeMillis();
    private LongPollingResource _longPollingResource;

    @Override
    public synchronized void deregisterResource(LongPollingResource longPollingResource) {
        _longPollingResource = null;
    }

    @Override
    public synchronized void registerForChanges(LongPollingResource longPollingResource) {
        _longPollingResource = longPollingResource;
    }

    @Override
    public synchronized void messageReceived(ChatMessage message) {
        _messages.add(message);
        if (_longPollingResource != null)
            _longPollingResource.processIfNotProcessed();
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
