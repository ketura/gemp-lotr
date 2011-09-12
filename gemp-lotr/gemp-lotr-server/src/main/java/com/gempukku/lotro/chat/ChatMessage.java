package com.gempukku.lotro.chat;

import java.util.Date;

public class ChatMessage {
    private Date _when;
    private String _from;
    private String _message;

    public ChatMessage(Date when, String from, String message) {
        _when = when;
        _from = from;
        _message = message;
    }

    public String getFrom() {
        return _from;
    }

    public String getMessage() {
        return _message;
    }

    public Date getWhen() {
        return _when;
    }
}
