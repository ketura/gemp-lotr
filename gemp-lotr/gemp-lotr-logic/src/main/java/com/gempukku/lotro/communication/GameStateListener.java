package com.gempukku.lotro.communication;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.game.decisions.AwaitingDecision;
import com.gempukku.lotro.game.state.GameStats;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface GameStateListener {
    public void cardCreated(LotroPhysicalCard card);
    public void cardCreated(LotroPhysicalCard card, boolean overridePlayerVisibility);

    public void cardMoved(LotroPhysicalCard card);

    public void cardsRemoved(String playerPerforming, Collection<LotroPhysicalCard> cards);

    public void initializeBoard(List<String> playerIds, boolean discardIsPublic);

    public void setPlayerPosition(String playerId, int i);

    public void setPlayerDecked(String playerId, boolean bool);

    public void setTwilight(int twilightPool);

    public void setTribbleSequence(String tribbleSequence);

    public void setCurrentPlayerId(String playerId);

    public void setCurrentPhase(String currentPhase);

    public void addAssignment(LotroPhysicalCard fp, Set<LotroPhysicalCard> minions);

    public void removeAssignment(LotroPhysicalCard fp);

    public void startSkirmish(LotroPhysicalCard fp, Set<LotroPhysicalCard> minions);

    public void addToSkirmish(LotroPhysicalCard card);

    public void removeFromSkirmish(LotroPhysicalCard card);

    public void finishSkirmish();

    public void addTokens(LotroPhysicalCard card, Token token, int count);

    public void removeTokens(LotroPhysicalCard card, Token token, int count);

    public void sendMessage(String message);

    public void setSite(LotroPhysicalCard card);

    public void sendGameStats(GameStats gameStats);

    public void cardAffectedByCard(String playerPerforming, LotroPhysicalCard card, Collection<LotroPhysicalCard> affectedCard);

    public void eventPlayed(LotroPhysicalCard card);

    public void cardActivated(String playerPerforming, LotroPhysicalCard card);

    public void decisionRequired(String playerId, AwaitingDecision awaitingDecision);

    public void sendWarning(String playerId, String warning);

    public void endGame();
}
