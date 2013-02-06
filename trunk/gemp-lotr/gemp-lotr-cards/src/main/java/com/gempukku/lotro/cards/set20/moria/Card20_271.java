package com.gempukku.lotro.cards.set20.moria;

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
 * 2
 * Goblin Scrapper
 * Moria	Minion • Goblin
 * 2	6	2
 * Response: If a [Moria] Goblin is about to take a wound, discard a card stacked on a [Moria] condition to prevent that wound.
 */
public class Card20_271 extends AbstractMinion {
    public Card20_271() {
        super(2, 2, 6, 2, Race.GOBLIN, Culture.MORIA, "Goblin Scrapper");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayBeforeActions(String playerId, LotroGame game, Effect effect, PhysicalCard self) {
        if (TriggerConditions.isGettingWounded(effect,  game, Culture.MORIA, Race.GOBLIN)
                && PlayConditions.isActive(game, Culture.MORIA, CardType.CONDITION, Filters.hasStacked(Filters.any))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.MORIA, CardType.CONDITION), Filters.any));
            action.appendEffect(
                    new ChooseAndPreventCardEffect(self, (WoundCharactersEffect) effect, playerId, "Choose a MORIA Goblin to prevent wound to", Culture.MORIA, Race.GOBLIN));
            return Collections.singletonList(action);
        }
        return null;
    }
}
