package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.ConditionEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * 0
 * Find the Halfling!
 * Event • Skirmish
 * Make an Uruk-hai strength + 2 (or strength +3 if you can spot a Hobbit companion that is not assigned to a skirmish.)
 * http://lotrtcg.org/coreset/isengard/findthehalfling(r1).png
 */
public class Card20_227 extends AbstractEvent {
    public Card20_227() {
        super(Side.SHADOW, 0, Culture.ISENGARD, "Find the Halfling!", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new ConditionEvaluator(2, 3, new SpotCondition(Race.HOBBIT, CardType.COMPANION, Filters.notAssignedToSkirmish)), Race.URUK_HAI));
        return action;
    }
}
