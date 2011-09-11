package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.ExertCharacterEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;

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
        super(2, 3, 2, 3, Keyword.HOBBIT, Culture.SHIRE, "Bilbo", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && PlayConditions.canExert(game.getGameState(), game.getModifiersQuerying(), self)) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Exert Bilbo to shuffle a SHIRE card from your discard pile into your draw deck.");
            action.addCost(new ExertCharacterEffect(self));
            action.addEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose SHIRE card", game.getGameState().getDiscard(playerId), Filters.culture(Culture.SHIRE), 1, 1) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            PhysicalCard card = selectedCards.get(0);
                            action.addEffect(new PutCardFromDiscardOnBottomOfDeckEffect(card));
                            action.addEffect(new ShuffleDeckEffect(playerId));
                        }
                    }
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
