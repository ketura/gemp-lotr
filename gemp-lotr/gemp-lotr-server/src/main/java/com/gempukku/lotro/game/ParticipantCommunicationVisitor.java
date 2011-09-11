package com.gempukku.lotro.game;

import com.gempukku.lotro.GameEvent;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.timing.Action;

public interface ParticipantCommunicationVisitor {
    public void visitWarning(String warning);

    public void visitAwaitingDecision(Action currentAction, AwaitingDecision awaitingDecision);

    public void visitGameEvent(GameEvent gameEvent);
}
