package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;

import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Event • Fellowship
 * Game Text: Spot a [GONDOR] companion to reveal the top 5 cards of an opponent's draw deck. For each Shadow card
 * revealed, add a threat. For each Free Peoples card revealed, place a [GONDOR] token on one of your conditions with
 * a [GONDOR] token on it. Shuffle that draw deck.
 */
public class Card7_104 extends AbstractEvent {
    public Card7_104() {
        super(Side.FREE_PEOPLE, 1, Culture.GONDOR, "Hidden Knowledge", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(final String opponentId) {
                        action.insertEffect(
                                new RevealTopCardsOfDrawDeckEffect(self, opponentId, 5) {
                                    @Override
                                    protected void cardsRevealed(List<PhysicalCard> cards) {
                                        int fpCards = 0;
                                        int shadowCards = 0;
                                        for (PhysicalCard card : cards) {
                                            if (card.getBlueprint().getSide() == Side.FREE_PEOPLE)
                                                fpCards++;
                                            else
                                                shadowCards++;
                                        }

                                        if (shadowCards > 0)
                                            action.appendEffect(
                                                    new AddThreatsEffect(playerId, self, shadowCards));
                                        for (int i = 0; i < fpCards; i++)
                                            action.appendEffect(
                                                    new ChooseActiveCardEffect(self, playerId, "Choose condition with a GONDOR token", Filters.owner(self.getOwner()), CardType.CONDITION, Filters.hasToken(Token.GONDOR)) {
                                                        @Override
                                                        protected void cardSelected(LotroGame game, PhysicalCard card) {
                                                            action.insertEffect(
                                                                    new AddTokenEffect(self, card, Token.GONDOR));
                                                        }
                                                    });
                                        action.appendEffect(
                                                new ShuffleDeckEffect(opponentId));
                                    }
                                });
                    }
                });
        return action;
    }
}
