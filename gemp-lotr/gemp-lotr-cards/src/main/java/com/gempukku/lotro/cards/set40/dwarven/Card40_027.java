package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Second Wind
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition - Support Area
 * Card Number: 1C27
 * Game Text: At the beginning of the maneuver phase, you may discard a card stacked on this condition to heal a Dwarf companion.
 */
public class Card40_027 extends AbstractPermanent{
    public Card40_027() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, "Second Wind", null, true);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canDiscardFromStacked(self, game, playerId, 1, self, Filters.any)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, self, Filters.any));
            action.appendEffect(
                    new ChooseAndHealCharactersEffect(action, playerId, Race.DWARF, CardType.COMPANION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
