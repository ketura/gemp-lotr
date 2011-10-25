package com.gempukku.lotro.communication;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.timing.GameStats;

import java.util.Collection;
import java.util.List;

public interface GameStateListener {
    public void cardCreated(PhysicalCard card);

    public void cardMoved(PhysicalCard card);

    public void cardsRemoved(String playerPerforming, Collection<PhysicalCard> cards);

    public void setPlayerOrder(List<String> playerIds);

    public void setPlayerPosition(String playerId, int i);

    public void setTwilight(int twilightPool);

    public void setCurrentPlayerId(String playerId);

    public void setCurrentPhase(Phase currentPhase);

    public void addAssignment(PhysicalCard fp, List<PhysicalCard> minions);

    public void removeAssignment(PhysicalCard fp);

    public void startSkirmish(PhysicalCard fp, List<PhysicalCard> minions);

    public void removeFromSkirmish(PhysicalCard card);

    public void finishSkirmish();

    public void addTokens(PhysicalCard card, Token token, int count);

    public void removeTokens(PhysicalCard card, Token token, int count);

    public void sendMessage(String message);

    public void setSite(PhysicalCard card);

    public void sendGameStats(GameStats gameStats);

    public void cardAffectedByCard(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCard);

    public void eventPlayed(PhysicalCard card);

    public void cardActivated(String playerPerforming, PhysicalCard card);
}
