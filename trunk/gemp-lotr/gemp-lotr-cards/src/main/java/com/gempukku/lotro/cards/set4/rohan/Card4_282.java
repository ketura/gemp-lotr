package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Skirmish: Make an unbound companion strength +1 for each [ROHAN] Man you spot (limit +3).
 */
public class Card4_282 extends AbstractOldEvent {
    public Card4_282() {
        super(Side.FREE_PEOPLE, Culture.ROHAN, "An Honorable Charge", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new CountSpottableEvaluator(3, Culture.ROHAN, Race.MAN),
                        Filters.unboundCompanion));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
