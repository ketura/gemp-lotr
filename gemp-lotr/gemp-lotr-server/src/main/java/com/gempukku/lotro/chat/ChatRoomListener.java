package com.gempukku.lotro.chat;

public interface ChatRoomListener {
    void messageReceived(ChatMessage message);
    void userJoined(String userId);
    void userParted(String userId);
    void listenerPushedOut();
}
