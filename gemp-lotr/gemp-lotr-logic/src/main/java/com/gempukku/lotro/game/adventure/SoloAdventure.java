package com.gempukku.lotro.game.adventure;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.DefaultGame;
import com.gempukku.lotro.game.actions.DefaultActionsEnvironment;
import com.gempukku.lotro.game.modifiers.CantDiscardFromPlayModifier;
import com.gempukku.lotro.game.modifiers.CantReturnToHandModifier;
import com.gempukku.lotro.game.modifiers.ModifiersLogic;

public abstract class SoloAdventure implements Adventure {
    @Override
    public void applyAdventureRules(DefaultGame game, DefaultActionsEnvironment actionsEnvironment, ModifiersLogic modifiersLogic) {
        modifiersLogic.addAlwaysOnModifier(
                new CantDiscardFromPlayModifier(null, "Can't be discarded from play", CardType.ADVENTURE, Filters.any));
        modifiersLogic.addAlwaysOnModifier(
                new CantReturnToHandModifier(null, "Can't be returned to hand", CardType.ADVENTURE, Filters.any));
    }
}
