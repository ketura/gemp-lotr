package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.CardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.Set;

/**
 * ❸ Discernment [Gan]
 * Event • Fellowship
 * Spell.
 * Exert Gandalf twice and add (X) to discard up to four Shadow conditions, with a combined twilight cost of X or less
 * <p/>
 * http://lotrtcg.org/coreset/gandalf/discernment(r3).jpg
 */
public class Card20_155 extends AbstractEvent {
    public Card20_155() {
        super(Side.FREE_PEOPLE, 3, Culture.GANDALF, "Discernment", Phase.FELLOWSHIP);
        addKeyword(Keyword.SPELL);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canExert(self, game, 2, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, final int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, 2, Filters.gandalf));
        action.appendCost(
                new PlayoutDecisionEffect(playerId,
                        new IntegerAwaitingDecision(1, "Choose X", 0, Integer.MAX_VALUE, 0) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                final int x = getValidatedResult(result);
                                action.appendCost(
                                        new AddTwilightEffect(self, x));
                                action.appendEffect(
                                        new PlayoutDecisionEffect(playerId,
                                                new CardsSelectionDecision(1, "Choose up to 4 Shadow conditions with combined twilight cost of up to " + x,
                                                        Filters.filterActive(game.getGameState(), game.getModifiersQuerying(), Side.SHADOW, CardType.CONDITION, Filters.canBeDiscarded(self)), 0, 4) {
                                                    @Override
                                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                                        Set<PhysicalCard> selectedCardsByResponse = getSelectedCardsByResponse(result);
                                                        int twilightTotal = 0;
                                                        for (PhysicalCard physicalCard : selectedCardsByResponse)
                                                            twilightTotal += physicalCard.getBlueprint().getTwilightCost();
                                                        if (twilightTotal > x)
                                                            throw new DecisionResultInvalidException("You have chosen conditions of combined twilight cost of " + twilightTotal);
                                                        action.appendEffect(
                                                                new DiscardCardsFromPlayEffect(self.getOwner(), self, Filters.in(selectedCardsByResponse)));
                                                    }
                                                }));
                            }
                        }));
        return action;
    }
}
