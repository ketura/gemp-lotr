package com.gempukku.lotro.chat;

import java.util.Date;

public class ChatMessage {
    private final Date _when;
    private final String _from;
    private final String _message;
    private final boolean fromAdmin;

    public ChatMessage(Date when, String from, String message, boolean fromAdmin) {
        _when = when;
        _from = from;
        _message = message;
        this.fromAdmin = fromAdmin;
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

    public boolean isFromAdmin() {
        return fromAdmin;
    }
}
