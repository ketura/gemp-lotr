package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.TriggerConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Howler
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion - Goblin
 * Strength: 5
 * Vitality: 2
 * Home: 4
 * Card Number: 1U162
 * Game Text: Each time you play a [MORIA] minion from a [MORIA] condition, you may make this minion strength +1 until the regroup phase.
 */
public class Card40_162 extends AbstractMinion {
    public Card40_162() {
        super(2, 5, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Howler");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.playedFromStacked(game, effectResult, Filters.and(Culture.MORIA, CardType.CONDITION), Culture.MORIA, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new StrengthModifier(self, self, 1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
