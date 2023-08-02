package com.gempukku.lotro.game;

import com.gempukku.lotro.gamestate.GameEvent;

import java.util.Map;

public interface ParticipantCommunicationVisitor {
    public void visitChannelNumber(int channelNumber);

    public void visitClock(Map<String, Integer> secondsLeft);

    public void visitGameEvent(GameEvent gameEvent);
}
