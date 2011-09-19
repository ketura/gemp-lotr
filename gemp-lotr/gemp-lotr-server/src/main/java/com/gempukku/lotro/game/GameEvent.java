package com.gempukku.lotro.game;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Token;
import com.gempukku.lotro.common.Zone;

import java.util.List;

public class GameEvent {
    public enum Type {
        PARTICIPANT, GAME_PHASE_CHANGE, TURN_CHANGE, PLAYER_POSITION, TWILIGHT_POOL,
        PUT_CARD_IN_PLAY, MOVE_CARD_IN_PLAY, REMOVE_CARD_FROM_PLAY,
        ADD_ASSIGNMENT, REMOVE_ASSIGNMENT,
        START_SKIRMISH, END_SKIRMISH,
        ADD_TOKENS, REMOVE_TOKENS,
        MESSAGE, WARNING,
        ZONE_SIZE
    }

    private String _message;
    private Type _type;
    private Zone _zone;
    private String _participantId;
    private List<String> _allParticipantIds;
    private Integer _index;
    private String _blueprintId;
    private Integer _cardId;
    private Integer _targetCardId;
    private Phase _phase;
    private Integer _count;
    private Token _token;
    private int[] _opposingCardIds;

    public GameEvent(Type type) {
        _type = type;
    }

    public Integer getIndex() {
        return _index;
    }

    public GameEvent index(int index) {
        _index = index;
        return this;
    }

    public Type getType() {
        return _type;
    }

    public Zone getZone() {
        return _zone;
    }

    public GameEvent zone(Zone zone) {
        _zone = zone;
        return this;
    }

    public Token getToken() {
        return _token;
    }

    public GameEvent token(Token token) {
        _token = token;
        return this;
    }

    public String getMessage() {
        return _message;
    }

    public GameEvent message(String message) {
        _message = message;
        return this;
    }

    public Integer getCount() {
        return _count;
    }

    public GameEvent count(int count) {
        _count = count;
        return this;
    }

    public int[] getOpposingCardIds() {
        return _opposingCardIds;
    }

    public GameEvent opposingCardIds(int[] opposingCardIds) {
        _opposingCardIds = opposingCardIds;
        return this;
    }

    public String getParticipantId() {
        return _participantId;
    }

    public GameEvent participantId(String participantId) {
        _participantId = participantId;
        return this;
    }

    public List<String> getAllParticipantIds() {
        return _allParticipantIds;
    }

    public GameEvent allParticipantIds(List<String> allParticipantIds) {
        _allParticipantIds = allParticipantIds;
        return this;
    }

    public GameEvent card(PhysicalCard physicalCard) {
        GameEvent gameEvent = cardId(physicalCard.getCardId()).blueprintId(physicalCard.getBlueprintId()).participantId(physicalCard.getOwner()).zone(physicalCard.getZone());
        PhysicalCard attachedTo = physicalCard.getAttachedTo();
        if (attachedTo != null)
            gameEvent = gameEvent.targetCardId(attachedTo.getCardId());
        PhysicalCard stackedOn = physicalCard.getStackedOn();
        if (stackedOn != null)
            gameEvent = gameEvent.targetCardId(stackedOn.getCardId());
        if (physicalCard.getBlueprint().getCardType() == CardType.SITE)
            gameEvent = gameEvent.index(physicalCard.getBlueprint().getSiteNumber());
        return gameEvent;
    }

    public String getBlueprintId() {
        return _blueprintId;
    }

    public GameEvent blueprintId(String blueprintId) {
        _blueprintId = blueprintId;
        return this;
    }

    public Integer getCardId() {
        return _cardId;
    }

    public GameEvent cardId(int cardId) {
        _cardId = cardId;
        return this;
    }

    public Integer getTargetCardId() {
        return _targetCardId;
    }

    public GameEvent targetCardId(int targetCardId) {
        _targetCardId = targetCardId;
        return this;
    }

    public Phase getPhase() {
        return _phase;
    }

    public GameEvent phase(Phase phase) {
        _phase = phase;
        return this;
    }
}
