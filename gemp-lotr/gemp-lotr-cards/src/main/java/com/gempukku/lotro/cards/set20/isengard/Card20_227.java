package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 0
 * Find the Halfling!
 * Isengard	Event • Skirmish
 * Make an Uruk-hai strength + 2 (or strength +3 if you can spot a Hobbit that is not assigned to a skirmish.)
 */
public class Card20_227 extends AbstractEvent {
    public Card20_227() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Find the Halfling!", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 3, new SpotCondition(Race.HOBBIT, Filters.notAssignedToSkirmish)), Race.URUK_HAI));
        return action;
    }
}
