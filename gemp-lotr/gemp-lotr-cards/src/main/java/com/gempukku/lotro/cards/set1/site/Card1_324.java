package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.CostToEffectAction;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Type: Site
 * Site: 1
 * Game Text: Fellowship: Add a burden to play Aragorn from your draw deck.
 */
public class Card1_324 extends AbstractSite {
    public Card1_324() {
        super("The Prancing Pony", "1_324", 1, 0, Direction.LEFT);
    }

    @Override
    public List<? extends Action> getPlayablePhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseSiteDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)) {
            final CostToEffectAction action = new CostToEffectAction(self, "Add a burder to play Aragorn from your draw deck.");
            action.addCost(new AddBurdenEffect(playerId));

            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ArbitraryCardsSelectionDecision(1, "Choose Aragorn to play from your deck", Filters.filter(game.getGameState().getDeck(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.name("Aragorn"),
                                    new Filter() {
                                        @Override
                                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                            List<? extends Action> playableActions = physicalCard.getBlueprint().getPlayablePhaseActions(playerId, game, physicalCard);
                                            return (playableActions != null && playableActions.size() > 0);
                                        }
                                    }), 0, 1) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                                    if (selectedCards.size() > 0) {
                                        PhysicalCard selectedCard = selectedCards.get(0);
                                        game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayablePhaseActions(playerId, game, selectedCard).get(0));
                                    }
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
