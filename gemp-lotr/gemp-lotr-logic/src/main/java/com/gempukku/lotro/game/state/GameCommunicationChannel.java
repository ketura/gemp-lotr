package com.gempukku.lotro.game.state;

import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.decisions.AwaitingDecision;
import com.gempukku.lotro.logic.timing.GameStats;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.LongPollingResource;

import java.util.*;

import static com.gempukku.lotro.game.state.GameEvent.Type.*;

public class GameCommunicationChannel implements GameStateListener, LongPollableResource {
    private List<GameEvent> _events = new LinkedList<GameEvent>();
    private String _self;
    private long _lastConsumed = System.currentTimeMillis();
    private int _channelNumber;
    private LongPollingResource _longPollingResource;

    public GameCommunicationChannel(String self, int channelNumber) {
        _self = self;
        _channelNumber = channelNumber;
    }

    public int getChannelNumber() {
        return _channelNumber;
    }

    @Override
    public void setPlayerOrder(List<String> participants) {
        List<String> participantIds = new LinkedList<String>();
        for (String participant : participants)
            participantIds.add(participant);
        appendEvent(new GameEvent(P).participantId(_self).allParticipantIds(participantIds));
    }

    @Override
    public synchronized void deregisterResource(LongPollingResource longPollingResource) {
        _longPollingResource = null;
    }

    @Override
    public synchronized void registerForChanges(LongPollingResource longPollingResource) {
        _longPollingResource = longPollingResource;
    }

    private synchronized void appendEvent(GameEvent event) {
        _events.add(event);
        if (_longPollingResource != null)
            _longPollingResource.processIfNotProcessed();
    }

    private int[] getCardIds(Collection<PhysicalCard> cards) {
        int[] result = new int[cards.size()];
        int index = 0;
        for (PhysicalCard card : cards) {
            result[index] = card.getCardId();
            index++;
        }
        return result;
    }

    @Override
    public void addAssignment(PhysicalCard freePeople, Set<PhysicalCard> minions) {
        appendEvent(new GameEvent(AA).cardId(freePeople.getCardId()).otherCardIds(getCardIds(minions)));
    }

    @Override
    public void removeAssignment(PhysicalCard freePeople) {
        appendEvent(new GameEvent(RA).cardId(freePeople.getCardId()));
    }

    @Override
    public void startSkirmish(PhysicalCard freePeople, Set<PhysicalCard> minions) {
        GameEvent gameEvent = new GameEvent(SS).otherCardIds(getCardIds(minions));
        if (freePeople != null)
            gameEvent.cardId(freePeople.getCardId());
        appendEvent(gameEvent);
    }

    @Override
    public void addToSkirmish(PhysicalCard card) {
        appendEvent(new GameEvent(ATS).card(card));
    }

    @Override
    public void removeFromSkirmish(PhysicalCard card) {
        appendEvent(new GameEvent(RFS).card(card));
    }

    @Override
    public void finishSkirmish() {
        appendEvent(new GameEvent(ES));
    }

    @Override
    public void setCurrentPhase(String phase) {
        appendEvent(new GameEvent(GPC).phase(phase));
    }

    @Override
    public void cardCreated(PhysicalCard card) {
        if (card.getZone().isPublic() || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
            appendEvent(new GameEvent(PCIP).card(card));
    }

    @Override
    public void cardMoved(PhysicalCard card) {
        appendEvent(new GameEvent(MCIP).card(card));
    }

    @Override
    public void cardsRemoved(String playerPerforming, Collection<PhysicalCard> cards) {
        Set<PhysicalCard> removedCardsVisibleByPlayer = new HashSet<PhysicalCard>();
        for (PhysicalCard card : cards) {
            if (card.getZone().isPublic() || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
                removedCardsVisibleByPlayer.add(card);
        }
        if (removedCardsVisibleByPlayer.size() > 0)
            appendEvent(new GameEvent(RCFP).otherCardIds(getCardIds(removedCardsVisibleByPlayer)).participantId(playerPerforming));
    }

    @Override
    public void setPlayerPosition(String participant, int position) {
        appendEvent(new GameEvent(PP).participantId(participant).index(position));
    }

    @Override
    public void setTwilight(int twilightPool) {
        appendEvent(new GameEvent(TP).count(twilightPool));
    }

    @Override
    public void setCurrentPlayerId(String currentPlayerId) {
        appendEvent(new GameEvent(TC).participantId(currentPlayerId));
    }

    @Override
    public void addTokens(PhysicalCard card, Token token, int count) {
        appendEvent(new GameEvent(AT).card(card).token(token).count(count));
    }

    @Override
    public void removeTokens(PhysicalCard card, Token token, int count) {
        appendEvent(new GameEvent(RT).card(card).token(token).count(count));
    }

    @Override
    public void sendMessage(String message) {
        appendEvent(new GameEvent(M).message(message));
    }

    @Override
    public void setSite(PhysicalCard card) {
        appendEvent(new GameEvent(PCIP).card(card).index(card.getSiteNumber()));
    }

    @Override
    public void sendGameStats(GameStats gameStats) {
        appendEvent(new GameEvent(GS).gameStats(gameStats.makeACopy()));
    }

    @Override
    public void cardAffectedByCard(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        appendEvent(new GameEvent(CAC).card(card).participantId(playerPerforming).otherCardIds(getCardIds(affectedCards)));
    }

    @Override
    public void eventPlayed(PhysicalCard card) {
        appendEvent(new GameEvent(EP).card(card));
    }

    @Override
    public void cardActivated(String playerPerforming, PhysicalCard card) {
        appendEvent(new GameEvent(CA).card(card).participantId(playerPerforming));
    }

    public void decisionRequired(String playerId, AwaitingDecision decision) {
        if (playerId.equals(_self))
            appendEvent(new GameEvent(D).awaitingDecision(decision).participantId(playerId));
    }

    public List<GameEvent> consumeGameEvents() {
        updateLastAccess();
        List<GameEvent> result = _events;
        _events = new LinkedList<GameEvent>();
        return result;
    }

    public boolean hasGameEvents() {
        updateLastAccess();
        return _events.size()>0;
    }

    private void updateLastAccess() {
        _lastConsumed = System.currentTimeMillis();
    }

    public long getLastAccessed() {
        return _lastConsumed;
    }
}
