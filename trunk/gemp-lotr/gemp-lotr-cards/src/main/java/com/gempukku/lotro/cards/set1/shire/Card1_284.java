package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.ExertCharactersEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 2
 * Type: Ally • Home 3 • Hobbit
 * Strength: 2
 * Vitality: 3
 * Site: 3
 * Game Text: Fellowship: Exert Bilbo to shuffle a [SHIRE] card from your discard pile into your draw deck.
 */
public class Card1_284 extends AbstractAlly {
    public Card1_284() {
        super(2, Block.FELLOWSHIP, 3, 2, 3, Race.HOBBIT, Culture.SHIRE, "Bilbo", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(self, game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new ExertCharactersEffect(self, self));
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose SHIRE card", game.getGameState().getDiscard(playerId), Filters.culture(Culture.SHIRE), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            if (selectedCards.size() > 0) {
                                PhysicalCard card = selectedCards.iterator().next();
                                action.appendEffect(new PutCardFromDiscardOnBottomOfDeckEffect(card));
                                action.appendEffect(new ShuffleDeckEffect(playerId));
                            }
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
