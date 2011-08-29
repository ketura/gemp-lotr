package com.gempukku.lotro.game;

import com.gempukku.lotro.GameEvent;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;

public interface ParticipantCommunicationVisitor {
    public void visitWarning(String warning);

    public void visitAwaitingDecision(AwaitingDecision awaitingDecision);

    public void visitGameEvent(GameEvent gameEvent);
}
