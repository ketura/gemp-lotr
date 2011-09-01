package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractLotroCardBlueprint;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Fellowship: Spot an Elf to play a tale from your draw deck.
 */
public class Card1_035 extends AbstractLotroCardBlueprint {
    public Card1_035() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "The Council of Elrond");
        addKeyword(Keyword.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public List<? extends Action> getPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canPlayFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF))) {
            final PlayEventAction action = new PlayEventAction(self);
            action.addCost(new SpotEffect(Filters.keyword(Keyword.ELF)));
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ArbitraryCardsSelectionDecision(1, "Choose Tale to play from your deck", Filters.filter(game.getGameState().getDeck(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.TALE),
                                    new Filter() {
                                        @Override
                                        public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                            List<? extends Action> playableActions = physicalCard.getBlueprint().getPhaseActions(playerId, game, physicalCard);
                                            return (playableActions != null && playableActions.size() > 0);
                                        }
                                    }), 0, 1) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                                    if (selectedCards.size() > 0) {
                                        PhysicalCard selectedCard = selectedCards.get(0);
                                        game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPhaseActions(playerId, game, selectedCard).get(0));
                                    }
                                }
                            })
            );

            return Collections.singletonList(action);
        }
        return null;
    }
}
