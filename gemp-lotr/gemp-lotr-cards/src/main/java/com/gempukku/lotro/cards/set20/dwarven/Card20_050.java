package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 2
 * Farin, Dwarf of Erebor
 * Dwarven	Companion • Dwarf
 * 5	3	6
 * Damage +1.
 * At the start of the Regroup phase, you may discard a [dwarven] card stacked on a [dwarven] condition to remove X
 * where X is the twilight cost of the discarded card..
 */
public class Card20_050 extends AbstractCompanion {
    public Card20_050() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Farin", "Dwarf of Erebor", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.REGROUP)
                && PlayConditions.isActive(game, Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(Culture.DWARVEN))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new ChooseAndDiscardStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Culture.DWARVEN) {
                        @Override
                        protected void discardingCardsCallback(Collection<PhysicalCard> cards) {
                            if (cards.size() > 0) {
                                final PhysicalCard card = cards.iterator().next();
                                action.appendEffect(
                                        new RemoveTwilightEffect(card.getBlueprint().getTwilightCost()));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
