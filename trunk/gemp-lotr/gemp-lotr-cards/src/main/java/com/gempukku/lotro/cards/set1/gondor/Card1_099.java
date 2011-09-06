package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.ChooseOpponentEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.DrawCardEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Regroup: Exert a ranger to make an opponent shuffle his hand into his draw deck and draw 8 cards.
 */
public class Card1_099 extends AbstractEvent {
    public Card1_099() {
        super(Side.FREE_PEOPLE, Culture.GONDOR, "Change of Plans", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RANGER), Filters.canExert());
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addCost(
                new ChooseAndExertCharacterEffect(action, playerId, "Choose a ranger", true, Filters.keyword(Keyword.RANGER), Filters.canExert()));
        action.addEffect(
                new ChooseOpponentEffect(playerId) {
                    @Override
                    protected void opponentChosen(String opponentId) {
                        List<PhysicalCard> hand = new LinkedList<PhysicalCard>(game.getGameState().getHand(opponentId));
                        for (PhysicalCard physicalCard : hand)
                            game.getGameState().putCardOnBottomOfDeck(physicalCard);
                        game.getGameState().shuffleDeck(opponentId);

                        for (int i = 0; i < 8; i++)
                            action.addEffect(new DrawCardEffect(opponentId));
                    }
                });
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
