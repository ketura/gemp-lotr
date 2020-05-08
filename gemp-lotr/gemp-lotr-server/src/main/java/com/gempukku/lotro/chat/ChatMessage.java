package com.gempukku.lotro.chat;

import java.util.Date;

public class ChatMessage {
    private Date when;
    private String from;
    private String message;
    private boolean forced;

    public ChatMessage(Date when, String from, String message, boolean forced) {
        this.when = when;
        this.from = from;
        this.message = message;
        this.forced = forced;
    }

    public String getFrom() {
        return from;
    }

    public String getMessage() {
        return message;
    }

    public Date getWhen() {
        return when;
    }

    public boolean isForced() {
        return forced;
    }
}
