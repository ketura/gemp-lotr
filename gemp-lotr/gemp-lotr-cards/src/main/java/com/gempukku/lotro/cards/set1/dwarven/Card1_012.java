package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseCardsFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion � Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Damage +1. Fellowship: If the twilight pool has fewer than 2 twilight tokens, add (2) to place a card from
 * hand beneath your draw deck.
 */
public class Card1_012 extends AbstractCompanion {
    public Card1_012() {
        super(2, 6, 3, Culture.DWARVEN, Race.DWARF, Signet.ARAGORN, "Gimli", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && game.getGameState().getTwilightPool() < 2) {
            final ActivateCardAction action = new ActivateCardAction(self);

            action.appendCost(new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new ChooseCardsFromHandEffect(playerId, 1, 1, Filters.any()) {
                        @Override
                        protected void cardsSelected(LotroGame game, Collection<PhysicalCard> selectedCards) {
                            for (PhysicalCard selectedCard : selectedCards) {
                                action.appendEffect(new PutCardFromHandOnBottomOfDeckEffect(selectedCard));
                            }
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
