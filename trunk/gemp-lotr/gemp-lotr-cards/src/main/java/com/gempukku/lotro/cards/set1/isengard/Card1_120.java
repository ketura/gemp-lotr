package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ForEachBurdenYouSpotEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

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
        super(Side.SHADOW, 2, CardType.CONDITION, Culture.ISENGARD, Zone.SUPPORT, "Alive and Unspoiled");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.URUK_HAI));
    }

    @Override
    public PlayPermanentAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction action = super.getPlayCardAction(playerId, game, self, twilightModifier);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.race(Race.URUK_HAI)));
        return action;
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        final GameState gameState = game.getGameState();
        if (PlayConditions.canUseShadowCardDuringPhase(gameState, Phase.SHADOW, self, 3)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new RemoveTwilightEffect(3));
            final String fpPlayer = gameState.getCurrentPlayerId();
            if (game.getModifiersQuerying().canLookOrRevealCardsInHand(game.getGameState(), fpPlayer)) {
                action.appendEffect(
                        new ForEachBurdenYouSpotEffect(playerId) {
                            @Override
                            protected void burdensSpotted(int burdensSpotted) {
                                action.insertEffect(
                                        new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                                                new ArbitraryCardsSelectionDecision(1, "Choose card to discard", GameUtils.getRandomCards(gameState.getHand(fpPlayer), burdensSpotted), 0, 1) {
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
            return Collections.singletonList(action);
        }
        return null;
    }
}
