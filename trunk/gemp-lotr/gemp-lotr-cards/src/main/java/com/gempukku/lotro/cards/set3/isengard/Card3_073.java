package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.costs.ChooseAndDiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

import java.util.Collection;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Regroup: Discard X [ISENGARD] Orcs to make the Free Peoples player wound X companions.
 */
public class Card3_073 extends AbstractEvent {
    public Card3_073() {
        super(Side.SHADOW, Culture.ISENGARD, "The Trees Are Strong", Phase.REGROUP);
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayCost(action, playerId, 0, Integer.MAX_VALUE, Filters.culture(Culture.ISENGARD), Filters.race(Race.ORC)) {
                    @Override
                    protected void cardsSelected(Collection<PhysicalCard> cards, boolean success) {
                        super.cardsSelected(cards, success);
                        if (success) {
                            int discardedOrcs = cards.size();
                            action.appendEffect(
                                    new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), discardedOrcs, discardedOrcs, Filters.type(CardType.COMPANION)));
                        }
                    }
                });
        return action;
    }
}
