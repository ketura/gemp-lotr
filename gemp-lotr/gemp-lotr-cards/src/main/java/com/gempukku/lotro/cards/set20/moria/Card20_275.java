package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Guard Commander, Captain of the Underdeeps
 * Moria	Minion • Goblin
 * 7	2	4
 * Remove (3) to make Guard Commander strength +1 for every other [Moria] Goblin you can spot.
 */
public class Card20_275 extends AbstractMinion {
    public Card20_275() {
        super(3, 7, 2, 4, Race.GOBLIN, Culture.MORIA, "Guard Commander", "Captain of the Underdeeps", true);
    }


    @Override
    public List<? extends Action> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 3)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(3));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new CountSpottableEvaluator(Filters.not(self), Culture.MORIA, Race.GOBLIN), self));
            return Collections.singletonList(action);
        }
        return null;
    }
}
