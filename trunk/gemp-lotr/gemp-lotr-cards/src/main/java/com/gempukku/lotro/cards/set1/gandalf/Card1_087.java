package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseArbitraryCardsEffect;

import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship: Play a [GANDALF] character from your draw deck.
 */
public class Card1_087 extends AbstractEvent {
    public Card1_087() {
        super(Side.FREE_PEOPLE, Culture.GANDALF, "A Wizard Is Never Late", Phase.FELLOWSHIP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self) {
        return true;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseArbitraryCardsEffect(playerId, "Choose GANDALF character to play", game.getGameState().getDeck(playerId),
                        Filters.and(Filters.culture(Culture.GANDALF), Filters.or(Filters.type(CardType.COMPANION), Filters.type(CardType.ALLY)), Filters.playable(game)), 0, 1) {
                    @Override
                    protected void cardsSelected(List<PhysicalCard> selectedCards) {
                        if (selectedCards.size() > 0) {
                            PhysicalCard selectedCard = selectedCards.get(0);
                            game.getActionsEnvironment().addActionToStack(selectedCard.getBlueprint().getPlayCardAction(playerId, game, selectedCard, 0));
                        }
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
