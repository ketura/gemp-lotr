package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayPermanentAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
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
        super(Side.SHADOW, CardType.CONDITION, Culture.ISENGARD, "Alive and Unspoiled");
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return PlayConditions.canPayForShadowCard(game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.URUK_HAI), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayPermanentAction action = new PlayPermanentAction(self, Zone.SHADOW_SUPPORT);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose an Uruk-hai", true, Filters.culture(Culture.URUK_HAI), Filters.canExert()));
        return action;
    }

    @Override
    public List<? extends Action> getPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        final GameState gameState = game.getGameState();
        if (PlayConditions.canPlayCardDuringPhase(game, Phase.SHADOW, self)
                && checkPlayRequirements(playerId, game, self, 0)) {
            return Collections.singletonList(getPlayCardAction(playerId, game, self, 0));
        }

        if (PlayConditions.canUseShadowCardDuringPhase(gameState, Phase.SHADOW, self, 3)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Remove (3) and spot X burdens to make the Free Peoples player reveal X cards at random from hand. You may discard 1 revealed card.");
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
