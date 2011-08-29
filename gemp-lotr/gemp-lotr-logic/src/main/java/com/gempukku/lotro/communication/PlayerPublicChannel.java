package com.gempukku.lotro.communication;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.game.PhysicalCard;

import java.util.List;

public interface PlayerPublicChannel {
    public void setCurrentPlayerId(String currentPlayerId);

    public void setTwilightPool(int twilightPool);

    public void gamePhaseChanged(Phase phase);

    public void setPlayerOrder(List<String> participantsOrder);

    public void putCardIntoPlay(PhysicalCard physicalCard);

    public void removeCardFromPlay(PhysicalCard physicalCard);

    public void setPlayerPosition(String participant, int position);

    public void addAssignment(PhysicalCard freePeople, List<PhysicalCard> minions);

    public void removeAssignment(PhysicalCard freePeople);

    public void startSkirmish(PhysicalCard freePeople, List<PhysicalCard> minions);

    public void finishSkirmish();
}
