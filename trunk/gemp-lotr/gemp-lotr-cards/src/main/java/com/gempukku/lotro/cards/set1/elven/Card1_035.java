package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.SpotEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.ArbitraryCardsSelectionDecision;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Fellowship: Spot an Elf to play a tale from your draw deck.
 */
public class Card1_035 extends AbstractEvent {
    public Card1_035() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "The Council of Elrond", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF));
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(new SpotEffect(Filters.keyword(Keyword.ELF)));
        action.addEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new ArbitraryCardsSelectionDecision(1, "Choose Tale to play from your deck", Filters.filter(game.getGameState().getDeck(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.TALE),
                                new Filter() {
                                    @Override
                                    public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                        return physicalCard.getBlueprint().checkPlayRequirements(playerId, game, physicalCard);
                                    }
                                }), 0, 1) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                List<PhysicalCard> selectedCards = getSelectedCardsByResponse(result);
                                if (selectedCards.size() > 0) {
                                    PhysicalCard selectedCard = selectedCards.get(0);
                                    game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, 0));
                                }
                            }
                        })
        );
        return action;
    }
}
