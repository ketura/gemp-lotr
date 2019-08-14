package com.gempukku.lotro.cards.set3.isengard;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;

import java.util.Collection;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Shadow: Spot an [ISENGARD] minion to discard up to 3 cards from hand and draw an equal number of cards.
 */
public class Card3_049 extends AbstractEvent {
    public Card3_049() {
        super(Side.SHADOW, 1, Culture.ISENGARD, "Abandoning Reason for Madness", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Culture.ISENGARD, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 0, 3, Filters.any) {
                    @Override
                    protected void cardsBeingDiscardedCallback(Collection<PhysicalCard> cardsBeingDiscarded) {
                        int cardsCount = cardsBeingDiscarded.size();
                        action.appendEffect(
                                new DrawCardsEffect(action, playerId, cardsCount));
                    }
                });
        return action;
    }
}
