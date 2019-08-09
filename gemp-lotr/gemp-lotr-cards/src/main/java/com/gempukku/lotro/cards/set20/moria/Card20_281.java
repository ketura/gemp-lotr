package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Troll's Keyward, Keeper of the Beast
 * Moria	Minion • Goblin
 * 8	3	4
 * Skirmish: Exert this minion to make the Cave Troll of Moria strength +2 (or strength +3 if at an underground site).
 */
public class Card20_281 extends AbstractMinion {
    public Card20_281() {
        super(3, 8, 3, 4, Race.GOBLIN, Culture.MORIA, "Troll's Keyward", "Keep of the Beast", true);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SKIRMISH, self, 0)
                && PlayConditions.canSelfExert(self, game)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new SelfExertEffect(action, self));
            action.appendEffect(
                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                            new ConditionEvaluator(2, 3, new LocationCondition(Keyword.UNDERGROUND)), Filters.name(Names.caveTroll)));
            return Collections.singletonList(action);
        }
        return null;
    }
}
