package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make Smeagol strength +1 for each Ring-bound Hobbit you can spot.
 */
public class Card15_045 extends AbstractEvent {
    public Card15_045() {
        super(Side.FREE_PEOPLE, 1, Culture.GOLLUM, "Hurry Hobbitses", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(
                        action, self, playerId, new CountActiveEvaluator(Race.HOBBIT, Keyword.RING_BOUND), Filters.smeagol));
        return action;
    }
}
