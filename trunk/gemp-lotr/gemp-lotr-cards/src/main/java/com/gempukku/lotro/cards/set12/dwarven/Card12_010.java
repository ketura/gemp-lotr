package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 4
 * Type: Event • Fellowship
 * Game Text: Toil 2. (For each [DWARVEN] character you exert when playing this, its twilight cost is -2) Spot 2 Dwarves
 * to draw up to 4 cards.
 */
public class Card12_010 extends AbstractEvent {
    public Card12_010() {
        super(Side.FREE_PEOPLE, 4, Culture.DWARVEN, "No Pauses, No Spills", Phase.FELLOWSHIP);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose number of cards to draw", 1, 4, 4) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int count = getValidatedResult(result);
                                action.appendEffect(
                                        new DrawCardsEffect(action, playerId, count));
                            }
                        }));
        return action;
    }
}
