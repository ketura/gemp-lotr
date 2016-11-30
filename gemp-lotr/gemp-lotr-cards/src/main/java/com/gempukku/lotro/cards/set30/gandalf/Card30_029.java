package com.gempukku.lotro.cards.set30.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Main Deck
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Artifact • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be Gandalf. Each time Gandalf wins a skirmish, you may remove a doubt.
 */
public class Card30_029 extends AbstractAttachableFPPossession {
    public Card30_029() {
        super(2, 2, 0, Culture.GANDALF, CardType.ARTIFACT, PossessionClass.HAND_WEAPON, "Glamdring", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, Filters.gandalf)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new RemoveBurdenEffect(playerId, self));
            return Collections.singletonList(action);
        }
        return null;
    }
}