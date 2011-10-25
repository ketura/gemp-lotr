package com.gempukku.lotro.logic.timing;

public abstract class EffectResult {
    public enum Type {
        WOUND, KILL, HEAL, EXERT, DISCARD_FROM_PLAY, DISCARD_FROM_HAND,

        ASSIGNMENT, OVERWHELM_IN_SKIRMISH, RESOLVE_SKIRMISH,

        ADD_TWILIGHT,

        START_OF_PHASE,
        END_OF_PHASE,

        START_OF_TURN,
        END_OF_TURN,

        PLAY, ACTIVATE, DRAW_CARD_OR_PUT_INTO_HAND,

        PUT_ON_THE_ONE_RING, REMOVE_BURDEN, ADD_BURDEN,

        WHEN_MOVE_FROM, WHEN_FELLOWSHIP_MOVES, WHEN_MOVE_TO,
        REVEAL_CARDS_FROM_HAND,
        INITIATIVE_CHANGE
    }

    private Type _type;

    protected EffectResult(Type type) {
        _type = type;
    }

    public Type getType() {
        return _type;
    }
}
