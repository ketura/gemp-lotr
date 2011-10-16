package com.gempukku.lotro.game;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.communication.GameStateListener;
import static com.gempukku.lotro.game.GameEvent.Type.*;

import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

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
        _events.add(new GameEvent(PARTICIPANT).participantId(_self).allParticipantIds(participantIds));
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
        _events.add(new GameEvent(ADD_ASSIGNMENT).cardId(freePeople.getCardId()).otherCardIds(getCardIds(minions)));
    }

    @Override
    public void removeAssignment(PhysicalCard freePeople) {
        _events.add(new GameEvent(REMOVE_ASSIGNMENT).cardId(freePeople.getCardId()));
    }

    @Override
    public void startSkirmish(PhysicalCard freePeople, List<PhysicalCard> minions) {
        GameEvent gameEvent = new GameEvent(START_SKIRMISH).otherCardIds(getCardIds(minions));
        if (freePeople != null)
            gameEvent.cardId(freePeople.getCardId());
        _events.add(gameEvent);
    }

    @Override
    public void removeFromSkirmish(PhysicalCard card) {
        _events.add(new GameEvent(REMOVE_FROM_SKIRMISH).card(card));
    }

    @Override
    public void finishSkirmish() {
        _events.add(new GameEvent(END_SKIRMISH));
    }

    @Override
    public void setCurrentPhase(Phase phase) {
        _events.add(new GameEvent(GAME_PHASE_CHANGE).phase(phase));
    }

    @Override
    public void cardCreated(PhysicalCard card) {
        _events.add(new GameEvent(PUT_CARD_IN_PLAY).card(card));
    }

    @Override
    public void cardMoved(PhysicalCard card) {
        _events.add(new GameEvent(MOVE_CARD_IN_PLAY).card(card));
    }

    @Override
    public void cardsRemoved(Collection<PhysicalCard> cards) {
        _events.add(new GameEvent(REMOVE_CARD_FROM_PLAY).otherCardIds(getCardIds(cards)));
    }

    @Override
    public void setPlayerPosition(String participant, int position) {
        _events.add(new GameEvent(PLAYER_POSITION).participantId(participant).index(position));
    }

    @Override
    public void setTwilight(int twilightPool) {
        _events.add(new GameEvent(TWILIGHT_POOL).count(twilightPool));
    }

    @Override
    public void setCurrentPlayerId(String currentPlayerId) {
        _events.add(new GameEvent(TURN_CHANGE).participantId(currentPlayerId));
    }

    @Override
    public void addTokens(PhysicalCard card, Token token, int count) {
        _events.add(new GameEvent(ADD_TOKENS).card(card).token(token).count(count));
    }

    @Override
    public void removeTokens(PhysicalCard card, Token token, int count) {
        _events.add(new GameEvent(REMOVE_TOKENS).card(card).token(token).count(count));
    }

    @Override
    public void sendMessage(String message) {
        _events.add(new GameEvent(MESSAGE).message(message));
    }

    @Override
    public void setSite(PhysicalCard card) {
        _events.add(new GameEvent(PUT_CARD_IN_PLAY).card(card).index(card.getBlueprint().getSiteNumber()));
    }

    @Override
    public void setZoneSize(String playerId, Zone zone, int size) {
        _events.add(new GameEvent(ZONE_SIZE).participantId(playerId).zone(zone).count(size));
    }

    @Override
    public void cardAffectedByCard(String playerPerforming, PhysicalCard card, Collection<PhysicalCard> affectedCards) {
        _events.add(new GameEvent(CARD_AFFECTS_CARD).participantId(playerPerforming).card(card).otherCardIds(getCardIds(affectedCards)));
    }

    public void eventPlayed(PhysicalCard card) {
        _events.add(new GameEvent(EVENT_PLAYED).card(card));
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
