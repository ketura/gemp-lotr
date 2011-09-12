package com.gempukku.lotro;

import com.gempukku.lotro.chat.ChatMessage;
import com.gempukku.lotro.chat.ChatRoomListener;

import java.util.LinkedList;
import java.util.List;

public class GatheringChatRoomListener implements ChatRoomListener {
    private List<ChatMessage> _messages = new LinkedList<ChatMessage>();

    @Override
    public void messageReceived(ChatMessage message) {
        _messages.add(message);
    }

    public List<ChatMessage> consumeMessages() {
        List<ChatMessage> messages = _messages;
        _messages = new LinkedList<ChatMessage>();
        return messages;
    }
}
