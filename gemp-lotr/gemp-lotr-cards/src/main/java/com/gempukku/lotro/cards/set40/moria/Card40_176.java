package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Guard Commander, Captain of the Underdeeps
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 3
 * Type: Minion - Goblin
 * Strength: 7
 * Vitality: 2
 * Home: 4
 * Card Number: 1R176
 * Game Text: Skirmish: Remove (3) to make this minion strength +1 for every other [MORIA] Goblin you can spot.
 */
public class Card40_176 extends AbstractMinion {
    public Card40_176() {
        super(3, 7, 2, 4, Race.GOBLIN, Culture.MORIA, "Guard Commander", "Captain of the Underdeeps", true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, null,
                                    new CountSpottableEvaluator(Culture.MORIA, Race.GOBLIN, Filters.not(self)))));
            return Collections.singletonList(action);
        }
        return null;
    }
}
