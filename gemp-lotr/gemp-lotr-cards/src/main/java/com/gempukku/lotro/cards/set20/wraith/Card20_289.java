package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 1
 * Darkly Swift
 * Ringwraith	Event • Regroup
 * Discard a Nazgul and spot a companion with 3 resistance or less
 * to add a burden (or two burdens if you can spot a companion with 0 resistance).
 */
public class Card20_289 extends AbstractEvent {
    public Card20_289() {
        super(Side.SHADOW, 1, Culture.WRAITH, "Darkly Swift", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canDiscardFromPlay(self, game, Race.NAZGUL)
                && PlayConditions.canSpot(game, CardType.COMPANION, Filters.maxResistance(3));
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Race.NAZGUL));
        action.appendEffect(
                new AddBurdenEffect(playerId, self,
                        new ConditionEvaluator(1, 2, new SpotCondition(CardType.COMPANION, Filters.maxResistance(0)))));
        return action;
    }
}
