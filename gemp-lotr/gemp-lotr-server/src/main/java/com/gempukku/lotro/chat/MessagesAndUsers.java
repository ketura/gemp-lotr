package com.gempukku.lotro.chat;

import com.gempukku.lotro.chat.ChatMessage;

import java.util.Collection;

public class MessagesAndUsers {
    private Collection<ChatMessage> chatMessages;
    private Collection<String> users;

    public MessagesAndUsers(Collection<ChatMessage> chatMessages, Collection<String> users) {
        this.chatMessages = chatMessages;
        this.users = users;
    }

    public Collection<ChatMessage> getChatMessages() {
        return chatMessages;
    }

    public Collection<String> getUsers() {
        return users;
    }
}
