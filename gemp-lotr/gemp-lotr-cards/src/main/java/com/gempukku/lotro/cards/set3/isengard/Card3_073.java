package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

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
        super(Side.SHADOW, 1, Culture.ISENGARD, "The Trees Are Strong", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 0, Integer.MAX_VALUE, Culture.ISENGARD, Race.ORC) {
                    @Override
                    protected void cardsSelected(LotroGame game, Collection<PhysicalCard> cards) {
                        super.cardsSelected(game, cards);    //To change body of overridden methods use File | Settings | File Templates.
                        int discardedOrcs = cards.size();
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, game.getGameState().getCurrentPlayerId(), discardedOrcs, discardedOrcs, CardType.COMPANION));
                    }
                });
        return action;
    }
}
