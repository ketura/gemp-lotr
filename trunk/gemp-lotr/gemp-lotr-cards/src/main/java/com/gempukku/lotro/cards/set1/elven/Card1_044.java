package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.logic.effects.DiscardCardFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardEffect;

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
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Foul Creation", Phase.FELLOWSHIP);
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
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose an Elf", true, Filters.keyword(Keyword.ELF), Filters.canExert()));
        action.addCost(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        List<? extends PhysicalCard> hand = game.getGameState().getHand(opponentId);
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
                });
        return action;
    }
}
