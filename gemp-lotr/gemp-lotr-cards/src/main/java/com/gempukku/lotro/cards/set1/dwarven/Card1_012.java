package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTwilightEffect;
import com.gempukku.lotro.cards.effects.PutCardFromHandOnBottomOfDeckEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.effects.ChooseCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.Action;

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
        super(2, 6, 3, Culture.DWARVEN, Signet.ARAGORN, "Gimli", true);
        addKeyword(Keyword.DWARF);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && game.getGameState().getTwilightPool() < 2) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Add (2) to place a card from hand beneath your draw deck");

            action.addCost(new AddTwilightEffect(2));
            action.addEffect(
                    new ChooseCardsFromHandEffect(playerId, "Choose a card to place beneath your draw deck", 1, 1, Filters.zone(Zone.HAND), Filters.owner(playerId)) {
                        @Override
                        protected void cardsSelected(List<PhysicalCard> selectedCards) {
                            action.addEffect(new PutCardFromHandOnBottomOfDeckEffect(selectedCards.get(0)));
                        }
                    });

            return Collections.singletonList(action);
        }
        return null;
    }
}
