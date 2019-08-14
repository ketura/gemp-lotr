package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Race.DWARF);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
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
