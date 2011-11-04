package com.gempukku.lotro.game;

import com.gempukku.lotro.game.state.GameEvent;

import java.util.Map;

public interface ParticipantCommunicationVisitor {
    public void visitClock(Map<String, Integer> secondsLeft);

    public void visitGameEvent(GameEvent gameEvent);
}
