package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.ShuffleDeckEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 7
 * Vitality: 2
 * Site: 6
 * Game Text: When you play this minion, you may shuffle up to 2 [WRAITH] cards from your discard pile into your draw deck.
 */
public class Card1_258 extends AbstractMinion {
    public Card1_258() {
        super(2, 7, 2, 6, Race.ORC, Culture.SAURON, "Morgul Skulker");
    }

    @Override
    public List<? extends Action> getOptionalAfterTriggers(final String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, null, "Shuffle up to 2 WRAITH cards from your discard pile into your draw deck.");
            action.addEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose up to 2 WRAITH cards", game.getGameState().getDiscard(playerId), Filters.culture(Culture.WRAITH), 0, 2) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.addEffect(
                                        new PutCardFromDiscardOnBottomOfDeckEffect(selectedCard));
                            }
                            action.addEffect(
                                    new ShuffleDeckEffect(playerId));
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
