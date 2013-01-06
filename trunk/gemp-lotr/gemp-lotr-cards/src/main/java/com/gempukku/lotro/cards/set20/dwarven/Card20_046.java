package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.PutCardFromStackedIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseStackedCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.Collection;

/**
 * 1
 * Dwarven Frenzy
 * Dwarven	Event • Maneuver
 * Exert X Dwarves twice to take X [Dwarven] cards stacked on [Dwarven] conditions into hand
 * (and draw X cards if the fellowship is at a mountain or underground site).
 */
public class Card20_046 extends AbstractEvent {
    public Card20_046() {
        super(Side.FREE_PEOPLE, 1, Culture.DWARVEN, "Dwarven Frenzy", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        int maxX = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Race.DWARF, Filters.canExert(self, 2));
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose X", 0, maxX, maxX) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int x = getValidatedResult(result);
                                action.appendCost(
                                        new ChooseAndExertCharactersEffect(action, playerId, x, x, 2, Race.DWARF));
                                for (int i = 0; i < x; i++) {
                                    action.appendEffect(
                                            new ChooseStackedCardsEffect(action, playerId, 1, 1, Filters.and(Culture.DWARVEN, CardType.CONDITION), Culture.DWARVEN) {
                                                @Override
                                                protected void cardsChosen(Collection<PhysicalCard> stackedCards) {
                                                    for (PhysicalCard stackedCard : stackedCards) {
                                                        action.appendEffect(
                                                                new PutCardFromStackedIntoHandEffect(stackedCard));
                                                    }
                                                }
                                            });
                                }
                                if (PlayConditions.location(game, Filters.or(Keyword.MOUNTAIN, Keyword.UNDERGROUND)))
                                    action.appendEffect(
                                            new DrawCardsEffect(action, playerId, x));
                            }
                        }));
        return action;
    }
}
