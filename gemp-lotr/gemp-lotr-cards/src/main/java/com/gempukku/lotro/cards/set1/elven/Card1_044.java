package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.GameUtils;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.MultipleChoiceAwaitingDecision;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Exert an Elf to reveal an opponent's hand. You may discard a [ISENGARD] minion revealed to
 * draw 2 cards.
 */
public class Card1_044 extends AbstractEvent {
    public Card1_044() {
        super(Side.FREE_PEOPLE, CardType.EVENT, Culture.ELVEN, "Foul Creation", Phase.FELLOWSHIP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.ELF), Filters.canExert());
    }

    @Override
    public Action getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseActiveCardEffect(playerId, "Choose an Elf", Filters.keyword(Keyword.ELF), Filters.canExert()) {
                    @Override
                    protected void cardSelected(PhysicalCard elf) {
                        action.addCost(new ExertCharacterEffect(elf));
                    }
                });
        action.addCost(new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                new MultipleChoiceAwaitingDecision(1, "Choose an opponent", GameUtils.getOpponents(game, playerId)) {
                    @Override
                    protected void validDecisionMade(int index, String chosenOpponent) {
                        List<? extends PhysicalCard> hand = game.getGameState().getHand(chosenOpponent);
                        List<PhysicalCard> isengardMinions = Filters.filter(hand, game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.ISENGARD), Filters.type(CardType.MINION));
                        action.addCost(
                                new ChooseArbitraryCardsEffect(playerId, "Choose ISENGARD minion to discard", isengardMinions, 1, 1) {
                                    @Override
                                    protected void cardsSelected(List<PhysicalCard> card) {
                                        action.addEffect(new DiscardCardFromHandEffect(card.get(0)));
                                        action.addEffect(new DrawCardEffect(playerId));
                                        action.addEffect(new DrawCardEffect(playerId));
                                    }
                                }
                        );
                    }
                })
        );
        return action;
    }
}
