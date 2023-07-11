package com.gempukku.lotro.game.decisions;

public class DecisionResultInvalidException extends Exception {
    private final String _warningMessage;

    public DecisionResultInvalidException() {
        this("Something went wrong");
    }

    public DecisionResultInvalidException(String warningMessage) {
        _warningMessage = warningMessage;
    }

    public String getWarningMessage() {
        return _warningMessage;
    }
}
