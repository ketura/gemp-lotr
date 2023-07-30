package com.gempukku.lotro.adventure;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.modifiers.CantReturnToHandModifier;
import com.gempukku.lotro.modifiers.ModifiersLogic;

public abstract class SoloAdventure implements Adventure {
    @Override
    public void applyAdventureRules(DefaultGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        modifiersLogic.addAlwaysOnModifier(
                new CantDiscardFromPlayModifier(null, "Can't be discarded from play", CardType.ADVENTURE, Filters.any));
        modifiersLogic.addAlwaysOnModifier(
                new CantReturnToHandModifier(null, "Can't be returned to hand", CardType.ADVENTURE, Filters.any));
    }
}
