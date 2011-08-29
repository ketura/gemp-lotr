package com.gempukku.lotro.logic.timing;

public abstract class EffectResult {
    public enum Type {
        WOUND, KILL,
        OVERWHELM_IN_SKIRMISH, RESOLVE_SKIRMISH,

        START_OF_PHASE,
        END_OF_PHASE,

        START_OF_TURN,
        END_OF_TURN,

        PLAY,

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
