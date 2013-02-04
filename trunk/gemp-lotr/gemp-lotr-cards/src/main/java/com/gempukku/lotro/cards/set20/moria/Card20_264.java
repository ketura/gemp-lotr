package com.gempukku.lotro.cards.set20.moria;

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
 * 1
 * Goblin Rallyer
 * Moria	Minion • Goblin
 * 5	1	4
 * When you play this minion, you may play a Goblin stacked on a [Moria] condition.
 */
public class Card20_264 extends AbstractMinion {
    public Card20_264() {
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
