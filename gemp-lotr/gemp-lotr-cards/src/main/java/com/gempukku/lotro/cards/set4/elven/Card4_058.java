package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 3
 * Type: Event
 * Game Text: Tale. Fellowship: Draw a card for each Elf companion you spot.
 */
public class Card4_058 extends AbstractEvent {
    public Card4_058() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Alliance Reforged", Phase.FELLOWSHIP);
        addKeyword(Keyword.TALE);
    }

    @Override
    public int getTwilightCost() {
        return 3;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new ForEachYouSpotDecision(1, "Choose number of Elf companions you wish to spot", game, Filters.and(Filters.race(Race.ELF), Filters.type(CardType.COMPANION)), Integer.MAX_VALUE) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int spotted = getValidatedResult(result);
                                action.appendEffect(
                                        new DrawCardEffect(playerId, spotted));
                            }
                        }));
        return action;
    }
}
