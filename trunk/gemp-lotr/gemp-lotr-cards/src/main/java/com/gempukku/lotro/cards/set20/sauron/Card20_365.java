package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Orc Miscreant
 * Sauron	Minion • Orc
 * 10	3	6
 * At the start of each skirmish involving this minion, you may remove a threat to play a [Sauron] skirmish event from your discard pile.
 */
public class Card20_365 extends AbstractMinion {
    public Card20_365() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Miscreant");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.isActive(game, self, Filters.inSkirmish)
                && PlayConditions.canRemoveThreat(game, self, 1)
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.SAURON, Keyword.SKIRMISH, CardType.EVENT)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.SAURON, Keyword.SKIRMISH, CardType.EVENT));
            return Collections.singletonList(action);
        }
        return null;
    }
}
