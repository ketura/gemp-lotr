package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.ShuffleCardsFromDiscardIntoDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Ally � Home 3 � Dwarf
 * Strength: 3
 * Vitality: 3
 * Site: 3
 * Game Text: Fellowship: Exert Grimir to shuffle a [DWARVEN] event from your discard pile into your draw deck.
 */
public class Card1_017 extends AbstractAlly {
    public Card1_017() {
        super(1, Block.FELLOWSHIP, 3, 3, 3, Race.DWARF, Culture.DWARVEN, "Grimir", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ExertCharactersEffect(self, self));

            Collection<PhysicalCard> discardedDwarvenEvents = Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.DWARVEN), CardType.EVENT);

            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose card to shuffle into draw deck", new LinkedList<PhysicalCard>(discardedDwarvenEvents), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            action.insertEffect(
                                    new ShuffleCardsFromDiscardIntoDeckEffect(self, playerId, selectedCards));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
