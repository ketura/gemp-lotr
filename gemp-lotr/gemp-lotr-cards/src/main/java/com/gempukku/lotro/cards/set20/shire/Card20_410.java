package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountSpottableEvaluator;

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
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountSpottableEvaluator(4, CardType.ALLY, Race.HOBBIT), Race.HOBBIT));
        return action;
    }
}
