package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.logic.actions.OptionalTriggerAction;

import java.util.HashSet;
import java.util.Set;

public abstract class EffectResult {
    private Set<String> _optionalTriggersUsed = new HashSet<String>();

    public enum Type {
        // Translated to new format
        ANY_NUMBER_KILLED, FOR_EACH_KILLED, FOR_EACH_HEALED,
        FOR_EACH_WOUNDED, FOR_EACH_EXERTED, FOR_EACH_DISCARDED_FROM_PLAY,

        FOR_EACH_DISCARDED_FROM_HAND,

        FREE_PEOPLE_PLAYER_STARTS_ASSIGNING,
        SKIRMISH_ABOUT_TO_END,
        THREAT_WOUND_TRIGGER,

        INITIATIVE_CHANGE,

        WHEN_MOVE_FROM, WHEN_FELLOWSHIP_MOVES, WHEN_MOVE_TO,

        START_OF_PHASE,
        END_OF_PHASE,

        START_OF_TURN,
        END_OF_TURN,

        RECONCILE,

        ZERO_VITALITY,

        PLAY, ACTIVATE,

        DRAW_CARD_OR_PUT_INTO_HAND,

        PUT_ON_THE_ONE_RING,

        REMOVE_BURDEN, ADD_BURDEN, ADD_THREAT,

        REVEAL_CARDS_FROM_HAND,

        SKIRMISH_FINISHED_WITH_OVERWHELM, SKIRMISH_FINISHED_NORMALLY,

        CHARACTER_WON_SKIRMISH, CHARACTER_LOST_SKIRMISH,

        CHARACTER_ASSIGNED
    }

    private Type _type;

    protected EffectResult(Type type) {
        _type = type;
    }

    public Type getType() {
        return _type;
    }

    public void optionalTriggerUsed(OptionalTriggerAction action) {
        _optionalTriggersUsed.add(action.getTriggerIdentifier());
    }

    public boolean wasOptionalTriggerUsed(OptionalTriggerAction action) {
        return _optionalTriggersUsed.contains(action.getTriggerIdentifier());
    }
}
