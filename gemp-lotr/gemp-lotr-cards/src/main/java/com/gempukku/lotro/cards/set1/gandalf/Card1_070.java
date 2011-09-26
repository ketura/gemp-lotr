package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ExertCharactersCost;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Ally • Home 1 • Man
 * Strength: 1
 * Vitality: 2
 * Site: 1
 * Game Text: Fellowship: Exert Barliman Butterbur to take a [GANDALF] event into hand from your discard pile.
 */
public class Card1_070 extends AbstractAlly {
    public Card1_070() {
        super(0, 1, 1, 2, Race.MAN, Culture.GANDALF, "Barliman Butterbur", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(new ExertCharactersCost(playerId, self));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose GANDALF event", game.getGameState().getDiscard(playerId), Filters.and(Filters.culture(Culture.GANDALF), Filters.type(CardType.EVENT)), 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> gandalfEvents) {
                            action.appendEffect(
                                    new PutCardFromDiscardIntoHandEffect(gandalfEvents.get(0)));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
