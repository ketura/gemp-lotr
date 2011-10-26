package com.gempukku.lotro.game.state;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.PhysicalCard;
import static com.gempukku.lotro.game.state.GameEvent.Type.*;
import com.gempukku.lotro.logic.timing.GameStats;

import java.util.*;

public class GatheringParticipantCommunicationChannel implements GameStateListener {
    private List<GameEvent> _events = new LinkedList<GameEvent>();
    private String _self;
    private Date _lastConsumed = new Date();

    public GatheringParticipantCommunicationChannel(String self) {
        _self = self;
    }

    @Override
    public void setPlayerOrder(List<String> participants) {
        List<String> participantIds = new LinkedList<String>();
        for (String participant : participants)
            participantIds.add(participant);
        _events.add(new GameEvent(P).participantId(_self).allParticipantIds(participantIds));
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
    public void addAssignment(PhysicalCard freePeople, List<PhysicalCard> minions) {
        _events.add(new GameEvent(AA).cardId(freePeople.getCardId()).otherCardIds(getCardIds(minions)));
    }

    @Override
    public void removeAssignment(PhysicalCard freePeople) {
        _events.add(new GameEvent(RA).cardId(freePeople.getCardId()));
    }

    @Override
    public void startSkirmish(PhysicalCard freePeople, List<PhysicalCard> minions) {
        GameEvent gameEvent = new GameEvent(SS).otherCardIds(getCardIds(minions));
        if (freePeople != null)
            gameEvent.cardId(freePeople.getCardId());
        _events.add(gameEvent);
    }

    @Override
    public void removeFromSkirmish(PhysicalCard card) {
        _events.add(new GameEvent(RFS).card(card));
    }

    @Override
    public void finishSkirmish() {
        _events.add(new GameEvent(ES));
    }

    @Override
    public void setCurrentPhase(Phase phase) {
        _events.add(new GameEvent(GPC).phase(phase));
    }

    @Override
    public void cardCreated(PhysicalCard card) {
        if (card.getZone().isPublic() || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
            _events.add(new GameEvent(PCIP).card(card));
    }

    @Override
    public void cardMoved(PhysicalCard card) {
        _events.add(new GameEvent(MCIP).card(card));
    }

    @Override
    public void cardsRemoved(String playerPerforming, Collection<PhysicalCard> cards) {
        Set<PhysicalCard> removedCardsVisibleByPlayer = new HashSet<PhysicalCard>();
        for (PhysicalCard card : cards) {
            if (card.getZone().isPublic() || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
                removedCardsVisibleByPlayer.add(card);
        }
        if (removedCardsVisibleByPlayer.size() > 0)
            _events.add(new GameEvent(RCFP).otherCardIds(getCardIds(removedCardsVisibleByPlayer)).participantId(playerPerforming));
    }

    @Override
    public void setPlayerPosition(String participant, int position) {
        _events.add(new GameEvent(PP).participantId(participant).index(position));
    }

    @Override
    public void setTwilight(int twilightPool) {
        _events.add(new GameEvent(TP).count(twilightPool));
    }

    @Override
    public void setCurrentPlayerId(String currentPlayerId) {
        _events.add(new GameEvent(TC).participantId(currentPlayerId));
    }

    @Override
    public void addTokens(PhysicalCard card, Token token, int count) {
        _events.add(new GameEvent(AT).card(card).token(token).count(count));
    }

    @Override
    public void removeTokens(PhysicalCard card, Token token, int count) {
        _events.add(new GameEvent(RT).card(card).token(token).count(count));
    }

    @Override
    public void sendMessage(String message) {
        _events.add(new GameEvent(M).message(message));
    }

    @Override
    public void setSite(PhysicalCard card) {
        _events.add(new GameEvent(PCIP).card(card).index(card.getBlueprint().getSiteNumber()));
    }

    @Override
    public void sendGameStats(GameStats gameStats) {
        _events.add(new GameEvent(GS).gameStats(gameStats.makeACopy()));
    }

    @Override
    public void cardAffectedByCard(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        _events.add(new GameEvent(CAC).card(card).participantId(playerPerforming).otherCardIds(getCardIds(affectedCards)));
    }

    @Override
    public void eventPlayed(PhysicalCard card) {
        _events.add(new GameEvent(EP).card(card));
    }

    @Override
    public void cardActivated(String playerPerforming, PhysicalCard card) {
        _events.add(new GameEvent(CA).card(card).participantId(playerPerforming));
    }

    public List<GameEvent> consumeGameEvents() {
        List<GameEvent> result = _events;
        _events = new LinkedList<GameEvent>();
        _lastConsumed = new Date();
        return result;
    }

    public Date getLastConsumed() {
        return _lastConsumed;
    }
}
