package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPreventCardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.WoundCharactersEffect;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.Collections;
import java.util.List;

/**
 * Title: Goblin Scrapper
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion - Goblin
 * Strength: 6
 * Vitality: 2
 * Home: 4
 * Card Number: 1U172
 * Game Text: Response: If a [MORIA] Goblin is about to take a wound, discard a card stacked on a [MORIA] condition to prevent that wound.
 */
public class Card40_172 extends AbstractMinion {
    public Card40_172() {
        super(2, 6, 2, 4, Race.GOBLIN, Culture.MORIA, "Goblin Scrapper");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect, game, Culture.MORIA, Race.GOBLIN)
        && PlayConditions.canDiscardFromStacked(self, game, playerId, 1, Filters.and(Culture.MORIA, CardType.CONDITION), Filters.any)) {
            WoundCharactersEffect woundEffect = (WoundCharactersEffect) effect;
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.MORIA, Race.GOBLIN), Filters.any));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, woundEffect, playerId, "Choose MORIA Goblin to prevent wound to", Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
