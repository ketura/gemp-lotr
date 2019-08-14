package com.gempukku.lotro.cards.set8.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Support Area
 * Game Text: To play, spot 2 [GONDOR] knights. Maneuver: Discard 2 cards from hand to reveal the top card of
 * an opponent’s draw deck. Choose an opponent who must discard a Shadow card that has a twilight cost that is the same
 * as the twilight cost of the revealed card.
 */
public class Card8_032 extends AbstractPermanent {
    public Card8_032() {
        super(Side.FREE_PEOPLE, 1, CardType.POSSESSION, Culture.GONDOR, "Catapult");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.GONDOR, Keyword.KNIGHT);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.MANEUVER, self)
                && PlayConditions.canDiscardFromHand(game, playerId, 2, Filters.any)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String revealOppponentId) {
                            action.insertEffect(
                                    new RevealTopCardsOfDrawDeckEffect(self, revealOppponentId, 1) {
                                        @Override
                                        protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                            for (PhysicalCard card : revealedCards) {
                                                final int twilightCost = card.getBlueprint().getTwilightCost();
                                                action.insertEffect(
                                                        new ChooseOpponentEffect(playerId) {
                                                            @Override
                                                            protected void opponentChosen(String discardOpponentId) {
                                                                action.insertEffect(
                                                                        new ChooseAndDiscardCardsFromPlayEffect(action, discardOpponentId, 1, 1, Side.SHADOW, Filters.printedTwilightCost(twilightCost)));
                                                            }
                                                        });
                                            }
                                        }
                                    });
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
