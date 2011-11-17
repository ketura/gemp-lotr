package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromDiscardOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseArbitraryCardsEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Minion • Orc
 * Strength: 5
 * Vitality: 1
 * Site: 4
 * Game Text: When you play this minion, you may place a [MORIA] Orc from your discard pile beneath your draw deck.
 */
public class Card1_181 extends AbstractMinion {
    public Card1_181() {
        super(1, 5, 1, 4, Race.ORC, Culture.MORIA, "Goblin Sneak");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (PlayConditions.played(game.getGameState(), game.getModifiersQuerying(), effectResult, Filters.sameCard(self))) {
            final OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new ChooseArbitraryCardsEffect(playerId, "Choose MORIA Orc", game.getGameState().getDiscard(playerId), Filters.and(Culture.MORIA, Filters.race(Race.ORC)), 1, 1) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendEffect(
                                        new PutCardFromDiscardOnBottomOfDeckEffect(selectedCard));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
