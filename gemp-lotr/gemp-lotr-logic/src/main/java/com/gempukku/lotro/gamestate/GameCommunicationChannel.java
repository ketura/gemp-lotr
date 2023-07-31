package com.gempukku.lotro.gamestate;

import com.gempukku.lotro.cards.lotronly.LotroPhysicalCard;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.common.Zone;
import com.gempukku.lotro.decisions.AwaitingDecision;
import com.gempukku.lotro.game.LotroFormat;
import com.gempukku.polling.LongPollableResource;
import com.gempukku.polling.WaitingRequest;

import java.util.*;

import static com.gempukku.lotro.gamestate.GameEvent.Type.*;

public class GameCommunicationChannel implements GameStateListener, LongPollableResource {
    private List<GameEvent> _events = Collections.synchronizedList(new LinkedList<>());
    private final String _self;
    private long _lastConsumed = System.currentTimeMillis();
    private final int _channelNumber;
    private volatile WaitingRequest _waitingRequest;

    private final LotroFormat _format;

    public GameCommunicationChannel(String self, int channelNumber, LotroFormat format) {
        _self = self;
        _channelNumber = channelNumber;
        _format = format;
    }

    public int getChannelNumber() {
        return _channelNumber;
    }

    @Override
    public void initializeBoard(List<String> participants, boolean discardIsPublic) {
        List<String> participantIds = new LinkedList<>();
        participantIds.addAll(participants);
        appendEvent(new GameEvent(PARTICIPANTS)
                .participantId(_self)
                .allParticipantIds(participantIds)
                .discardPublic(discardIsPublic)
        );
    }

    @Override
    public synchronized void deregisterRequest(WaitingRequest waitingRequest) {
        _waitingRequest = null;
    }

    @Override
    public synchronized boolean registerRequest(WaitingRequest waitingRequest) {
        if (_events.size()>0)
            return true;

        _waitingRequest = waitingRequest;
        return false;
    }

    private synchronized void appendEvent(GameEvent event) {
        _events.add(event);
        if (_waitingRequest != null) {
            _waitingRequest.processRequest();
            _waitingRequest = null;
        }
    }

    private int[] getCardIds(Collection<LotroPhysicalCard> cards) {
        int[] result = new int[cards.size()];
        int index = 0;
        for (LotroPhysicalCard card : cards) {
            result[index] = card.getCardId();
            index++;
        }
        return result;
    }

    @Override
    public void addAssignment(LotroPhysicalCard freePeople, Set<LotroPhysicalCard> minions) {
        appendEvent(new GameEvent(ADD_ASSIGNMENT).cardId(freePeople.getCardId()).otherCardIds(getCardIds(minions)));
    }

    @Override
    public void removeAssignment(LotroPhysicalCard freePeople) {
        appendEvent(new GameEvent(REMOVE_ASSIGNMENT).cardId(freePeople.getCardId()));
    }

    @Override
    public void startSkirmish(LotroPhysicalCard freePeople, Set<LotroPhysicalCard> minions) {
        GameEvent gameEvent = new GameEvent(START_SKIRMISH).otherCardIds(getCardIds(minions));
        if (freePeople != null)
            gameEvent.cardId(freePeople.getCardId());
        appendEvent(gameEvent);
    }

    @Override
    public void addToSkirmish(LotroPhysicalCard card) {
        appendEvent(new GameEvent(ADD_TO_SKIRMISH).card(card));
    }

    @Override
    public void removeFromSkirmish(LotroPhysicalCard card) {
        appendEvent(new GameEvent(REMOVE_FROM_SKIRMISH).card(card));
    }

    @Override
    public void finishSkirmish() {
        appendEvent(new GameEvent(END_SKIRMISH));
    }

    @Override
    public void setCurrentPhase(String phase) {
        appendEvent(new GameEvent(GAME_PHASE_CHANGE).phase(phase));
    }

    @Override
    public void cardCreated(LotroPhysicalCard card) {
        boolean publicDiscard = card.getZone() == Zone.DISCARD && _format.discardPileIsPublic();
        if (card.getZone().isPublic() || publicDiscard || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
            appendEvent(new GameEvent(PUT_CARD_INTO_PLAY).card(card));
    }

    @Override
    public void cardCreated(LotroPhysicalCard card, boolean overridePlayerVisibility) {
        boolean publicDiscard = card.getZone() == Zone.DISCARD && _format.discardPileIsPublic();
        if (card.getZone().isPublic() || publicDiscard || ((overridePlayerVisibility || card.getZone().isVisibleByOwner()) && card.getOwner().equals(_self)))
            appendEvent(new GameEvent(PUT_CARD_INTO_PLAY).card(card));
    }

    @Override
    public void cardMoved(LotroPhysicalCard card) {
        appendEvent(new GameEvent(MOVE_CARD_IN_PLAY).card(card));
    }

    @Override
    public void cardsRemoved(String playerPerforming, Collection<LotroPhysicalCard> cards) {
        Set<LotroPhysicalCard> removedCardsVisibleByPlayer = new HashSet<>();
        for (LotroPhysicalCard card : cards) {
            boolean publicDiscard = card.getZone() == Zone.DISCARD && _format.discardPileIsPublic();
            if (card.getZone().isPublic() || publicDiscard || (card.getZone().isVisibleByOwner() && card.getOwner().equals(_self)))
                removedCardsVisibleByPlayer.add(card);
        }
        if (removedCardsVisibleByPlayer.size() > 0)
            appendEvent(new GameEvent(REMOVE_CARD_FROM_PLAY).otherCardIds(getCardIds(removedCardsVisibleByPlayer)).participantId(playerPerforming));
    }

    @Override
    public void setPlayerPosition(String participant, int position) {
        appendEvent(new GameEvent(PLAYER_POSITION).participantId(participant).index(position));
    }

    @Override
    public void setPlayerDecked(String participant, boolean bool) {
        appendEvent(new GameEvent(PLAYER_DECKED).participantId(participant).bool(bool));
    }

    @Override
    public void setPlayerScore(String participant, int points) {
        appendEvent(new GameEvent(PLAYER_SCORE).participantId(participant).score(points));
    }

    @Override
    public void setTwilight(int twilightPool) {
        appendEvent(new GameEvent(TWILIGHT_POOL_UPDATE).count(twilightPool));
    }

    @Override
    public void setTribbleSequence(String tribbleSequence) {
        appendEvent(new GameEvent(TRIBBLE_SEQUENCE_UPDATE).message(tribbleSequence));
    }

    @Override
    public void setCurrentPlayerId(String currentPlayerId) {
        appendEvent(new GameEvent(TURN_CHANGE).participantId(currentPlayerId));
    }

    @Override
    public void addTokens(LotroPhysicalCard card, Token token, int count) {
        appendEvent(new GameEvent(ADD_TOKENS).card(card).token(token).count(count));
    }

    @Override
    public void removeTokens(LotroPhysicalCard card, Token token, int count) {
        appendEvent(new GameEvent(REMOVE_TOKENS).card(card).token(token).count(count));
    }

    @Override
    public void sendMessage(String message) {
        appendEvent(new GameEvent(SEND_MESSAGE).message(message));
    }

    @Override
    public void setSite(LotroPhysicalCard card) {
        appendEvent(new GameEvent(PUT_CARD_INTO_PLAY).card(card).index(card.getSiteNumber()));
    }

    @Override
    public void sendGameStats(GameStats gameStats) {
        appendEvent(new GameEvent(GAME_STATS).gameStats(gameStats.makeACopy()));
    }

    @Override
    public void cardAffectedByCard(String playerPerforming, LotroPhysicalCard card, Collection<LotroPhysicalCard> affectedCards) {
        appendEvent(new GameEvent(CARD_AFFECTED_BY_CARD).card(card).participantId(playerPerforming).otherCardIds(getCardIds(affectedCards)));
    }

    @Override
    public void eventPlayed(LotroPhysicalCard card) {
        appendEvent(new GameEvent(SHOW_CARD_ON_SCREEN).card(card));
    }

    @Override
    public void cardActivated(String playerPerforming, LotroPhysicalCard card) {
        appendEvent(new GameEvent(FLASH_CARD_IN_PLAY).card(card).participantId(playerPerforming));
    }

    public void decisionRequired(String playerId, AwaitingDecision decision) {
        if (playerId.equals(_self))
            appendEvent(new GameEvent(DECISION).awaitingDecision(decision).participantId(playerId));
    }

    @Override
    public void sendWarning(String playerId, String warning) {
        if (playerId.equals(_self))
            appendEvent(new GameEvent(SEND_WARNING).message(warning));
    }

    public List<GameEvent> consumeGameEvents() {
        updateLastAccess();
        List<GameEvent> result = _events;
        _events = Collections.synchronizedList(new LinkedList<>());
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

    @Override
    public void endGame() {
        appendEvent(new GameEvent(GAME_ENDED));
    }
}
