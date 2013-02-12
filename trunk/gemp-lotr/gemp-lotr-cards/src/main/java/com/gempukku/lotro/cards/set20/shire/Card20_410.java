package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * 1
 * Save the Shire
 * Shire	Event • Skirmish
 * Tale.
 * Make a Hobbit strength +1 for each Hobbit ally you can spot (limit +4).
 */
public class Card20_410 extends AbstractEvent {
    public Card20_410() {
        super(Side.FREE_PEOPLE, 1, Culture.SHIRE, "Save the Shire", Phase.SKIRMISH);
        addKeyword(Keyword.TALE);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountSpottableEvaluator(4, CardType.ALLY, Race.HOBBIT), Race.HOBBIT));
        return action;
    }
}
