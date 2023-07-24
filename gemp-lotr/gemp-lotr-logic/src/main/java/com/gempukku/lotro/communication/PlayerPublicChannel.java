package com.gempukku.lotro.communication;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;

import java.util.List;

public interface PlayerPublicChannel {
    public void setCurrentPlayerId(String currentPlayerId);

    public void setTwilightPool(int twilightPool);

    public void gamePhaseChanged(Phase phase);

    public void setPlayerOrder(List<String> participantsOrder);

    public void putCardIntoPlay(LotroPhysicalCard physicalCard);

    public void removeCardFromPlay(LotroPhysicalCard physicalCard);

    public void setPlayerPosition(String participant, int position);

    public void addAssignment(LotroPhysicalCard freePeople, List<LotroPhysicalCard> minions);

    public void removeAssignment(LotroPhysicalCard freePeople);

    public void startSkirmish(LotroPhysicalCard freePeople, List<LotroPhysicalCard> minions);

    public void finishSkirmish();
}
