package com.gempukku.lotro.logic.timing;

public abstract class EffectResult {
    public enum Type {
        WOUND, KILL, HEAL, EXERT, DISCARD_FROM_PLAY, DISCARD_FROM_HAND,

        FREE_PEOPLE_PLAYER_STARTS_ASSIGNING,

        ASSIGNMENT, OVERWHELM_IN_SKIRMISH, RESOLVE_SKIRMISH,

        ADD_TWILIGHT,

        START_OF_PHASE,
        END_OF_PHASE,

        START_OF_TURN,
        END_OF_TURN,

        RECONCILE,

        PLAY, ACTIVATE, DRAW_CARD_OR_PUT_INTO_HAND,

        PUT_ON_THE_ONE_RING, REMOVE_BURDEN, ADD_BURDEN, ADD_THREAT,

        SKIRMISH_ABOUT_TO_END,

        WHEN_MOVE_FROM, WHEN_FELLOWSHIP_MOVES, WHEN_MOVE_TO,
        REVEAL_CARDS_FROM_HAND,
        INITIATIVE_CHANGE,
        THREAT_WOUND_TRIGGER
    }

    private Type _type;

    protected EffectResult(Type type) {
        _type = type;
    }

    public Type getType() {
        return _type;
    }
}
