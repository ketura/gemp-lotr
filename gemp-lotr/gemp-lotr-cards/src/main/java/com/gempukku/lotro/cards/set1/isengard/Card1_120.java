package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: To play, exert an Uruk-hai. Plays to your support area. Shadow: Remove (3) and spot X burdens to make the
 * Free Peoples player reveal X cards at random from hand. You may discard 1 revealed card.
 */
public class Card1_120 extends AbstractPermanent {
    public Card1_120() {
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, "Alive and Unspoiled");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.URUK_HAI));
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(final String playerId, final LotroGame game, final PhysicalCard self) {
        final GameState gameState = game.getGameState();
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(3));
            final String fpPlayer = game.getGameState().getCurrentPlayerId();
            if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game, fpPlayer, playerId)) {
                action.appendEffect(
                        new ForEachBurdenYouSpotEffect(playerId) {
                            @Override
                            protected void burdensSpotted(int burdensSpotted) {
                                action.insertEffect(
                                        new RevealRandomCardsFromHandEffect(playerId, fpPlayer, self, burdensSpotted) {
                                            @Override
                                            protected void cardsRevealed(List<PhysicalCard> revealedCards) {
                                                if (revealedCards.size() > 0)
                                                    action.insertEffect(
                                                            new PlayoutDecisionEffect(playerId,
                                                                    new ArbitraryCardsSelectionDecision(1, "Choose card to discard", revealedCards, 0, 1) {
                                                                        @Override
                                                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                                                            List<PhysicalCard> cards = getSelectedCardsByResponse(result);
                                                                            if (cards.size() > 0)
                                                                                action.appendEffect(new DiscardCardsFromHandEffect(self, fpPlayer, cards, true));
                                                                        }
                                                                    }));

                                            }

                                        });
                            }
                        });
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
