package com.gempukku.lotro;

import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.communication.GameStateListener;
import com.gempukku.lotro.game.PhysicalCard;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static com.gempukku.lotro.GameEvent.Type.*;

public class GatheringParticipantCommunicationChannel implements GameStateListener {
    private List<GameEvent> _events = new LinkedList<GameEvent>();
    private String _self;
    private Date _lastConsumed;

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

    private int[] getCardIds(List<PhysicalCard> minions) {
        int[] result = new int[minions.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = minions.get(i).getCardId();
        return result;
    }

    @Override
    public void addAssignment(PhysicalCard freePeople, List<PhysicalCard> minions) {
        _events.add(new GameEvent(ADD_ASSIGNMENT).cardId(freePeople.getCardId()).opposingCardIds(getCardIds(minions)));
    }

    @Override
    public void removeAssignment(PhysicalCard freePeople) {
        _events.add(new GameEvent(REMOVE_ASSIGNMENT).cardId(freePeople.getCardId()));
    }

    @Override
    public void startSkirmish(PhysicalCard freePeople, List<PhysicalCard> minions) {
        _events.add(new GameEvent(START_SKIRMISH).cardId(freePeople.getCardId()).opposingCardIds(getCardIds(minions)));
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
    public void cardRemoved(PhysicalCard card) {
        _events.add(new GameEvent(REMOVE_CARD_FROM_PLAY).card(card));
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

    public void sendMessage(String message) {
        _events.add(new GameEvent(MESSAGE).message(message));
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
