package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromStackedEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Rallyer
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Minion - Goblin
 * Strength: 5
 * Vitality: 1
 * Home: 4
 * Card Number: 1C165
 * Game Text: When you play this minion, you may play a Goblin stacked on a [MORIA] condition.
 */
public class Card40_165 extends AbstractMinion {
    public Card40_165() {
        super(1, 5, 1, 4, Race.GOBLIN, Culture.MORIA, "Goblin Rallyer");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.canPlayFromStacked(playerId, game, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseAndPlayCardFromStackedEffect(playerId, Filters.and(Culture.MORIA, CardType.CONDITION), Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
