package com.gempukku.lotro.logic.timing;

public abstract class EffectResult {
    public enum Type {
        WOUND, KILL, HEAL, EXERT, DISCARD_FROM_PLAY,

        ASSIGNMENT, OVERWHELM_IN_SKIRMISH, RESOLVE_SKIRMISH,

        START_OF_PHASE,
        END_OF_PHASE,

        START_OF_TURN,
        END_OF_TURN,

        PLAY,

        PUT_ON_THE_ONE_RING, REMOVE_BURDEN,

        WHEN_MOVE_FROM, WHEN_FELLOWSHIP_MOVES, WHEN_MOVE_TO
    }

    private Type _type;

    protected EffectResult(Type type) {
        _type = type;
    }

    public Type getType() {
        return _type;
    }
}
