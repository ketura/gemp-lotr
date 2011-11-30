package com.gempukku.lotro.logic.timing;

import com.gempukku.lotro.game.PhysicalCard;

import java.util.HashSet;
import java.util.Set;

public abstract class EffectResult {
    private Set<Integer> _optionalTriggersUsedForCardId = new HashSet<Integer>();
    private Set<String> _optionalTriggersUsedForRuleName = new HashSet<String>();

    public enum Type {
        // Translated to new format
        ANY_NUMBER_KILLED, FOR_EACH_KILLED,
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

    public void optionalTriggerUsed(Action.ActionSource actionSource) {
        if (actionSource.getPhysicalCard() != null)
            _optionalTriggersUsedForCardId.add(actionSource.getPhysicalCard().getCardId());
        else
            _optionalTriggersUsedForRuleName.add(actionSource.getRuleName());
    }

    public boolean wasOptionalTriggerUsed(PhysicalCard card) {
        return _optionalTriggersUsedForCardId.contains(card.getCardId());
    }

    public boolean wasOptionalTriggerUsed(String ruleName) {
        return _optionalTriggersUsedForRuleName.contains(ruleName);
    }

    public boolean wasOptionalTriggerUsed(Action.ActionSource actionSource) {
        if (actionSource.getPhysicalCard() != null)
            return _optionalTriggersUsedForCardId.contains(actionSource.getPhysicalCard().getCardId());
        else
            return _optionalTriggersUsedForRuleName.contains(actionSource.getRuleName());
    }
}
