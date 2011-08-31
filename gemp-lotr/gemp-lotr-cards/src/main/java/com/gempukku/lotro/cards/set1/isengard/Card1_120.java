package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
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
public class Card1_120 extends AbstractLotroCardBlueprint {
    public Card1_120() {
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Alive and Unspoiled", "1_120");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        final GameState gameState = game.getGameState();
        if (PlayConditions.canPlayShadowCardDuringPhase(game, Phase.SHADOW, self)
                && Filters.canSpot(gameState, game.getModifiersQuerying(), Filters.culture(Culture.URUK_HAI), Filters.canExert())) {
            final PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose an Uruk-hai", Filters.culture(Culture.URUK_HAI), Filters.canExert()) {
                        @Override
                        protected void cardSelected(PhysicalCard urukHai) {
                            action.addCost(new ExertCharacterEffect(urukHai));
                        }
                    }
            );
            return Collections.singletonList(action);
        }

        if (PlayConditions.canUseShadowCardDuringPhase(gameState, Phase.SHADOW, self, 3)) {
            final CostToEffectAction action = new CostToEffectAction(self, Keyword.SHADOW, "Remove (3) and spot X burdens to make the Free Peoples player reveal X cards at random from hand. You may discard 1 revealed card.");
            action.addCost(new RemoveTwilightEffect(3));
            final String fpPlayer = gameState.getCurrentPlayerId();
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ArbitraryCardsSelectionDecision(1, "Choose card to discard", GameUtils.getRandomCards(gameState.getHand(fpPlayer), gameState.getBurdens(fpPlayer)), 0, 1) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    List<PhysicalCard> cards = getSelectedCardsByResponse(result);
                                    if (cards.size() > 0)
                                        action.addEffect(new DiscardCardFromHandEffect(cards.get(0)));
                                }
                            })
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
