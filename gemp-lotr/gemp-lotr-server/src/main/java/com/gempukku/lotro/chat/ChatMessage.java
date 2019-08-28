package com.gempukku.lotro.chat;

import java.util.Date;

public class ChatMessage {
    private Date _when;
    private String _from;
    private String _message;
    private boolean fromAdmin;

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
