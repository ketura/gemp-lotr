package com.gempukku.lotro.cards.set40.isengard;

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
 * Title: Find the Halfling!
 * Set: Second Edition
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 0
 * Type: Event - Skirmish
 * Card Number: 1C123
 * Game Text: Make an Uruk-hai strength +2 or make an Uruk-hai strength +3 if you can spot a Hobbit companion
 * that is not assigned to a skirmish.
 */
public class Card40_123 extends AbstractEvent {
    public Card40_123() {
        super(Side.FREE_PEOPLE, 0, Culture.ISENGARD, "Find the Halfling!", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId,
                        new ConditionEvaluator(2, 3, new SpotCondition(Race.HOBBIT, CardType.COMPANION, Filters.notAssignedToSkirmish)),
                        Race.URUK_HAI));
        return action;
    }
}
