package com.gempukku.lotro.game;

import com.gempukku.lotro.logic.decisions.AwaitingDecision;

import java.util.Map;

public interface ParticipantCommunicationVisitor {
    public void visitClock(Map<String, Integer> secondsLeft);

    public void visitAwaitingDecision(AwaitingDecision awaitingDecision);

    public void visitGameEvent(GameEvent gameEvent);

    public void visitSkirmishStats(int fpStrength, int shadowStrength);
}
