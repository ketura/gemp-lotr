package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Maneuver: Spot an Elf to make each opponent discard a card from his or her hand. Draw a card for each
 * card discarded in this way.
 */
public class Card3_011 extends AbstractOldEvent {
    public Card3_011() {
        super(Side.FREE_PEOPLE, Culture.ELVEN, "Cast It Into the Fire!", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        String[] opponents = GameUtils.getOpponents(game, playerId);
        final AtomicInteger integer = new AtomicInteger(0);
        for (String opponent : opponents) {
            action.appendEffect(
                    new ChooseAndDiscardCardsFromHandEffect(action, opponent, true, 1) {
                        @Override
                        protected void cardsBeingDiscarded(Collection<PhysicalCard> cardsBeingDiscarded, boolean success) {
                            integer.addAndGet(cardsBeingDiscarded.size());
                        }
                    });
        }
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.insertEffect(
                                new DrawCardEffect(playerId, integer.get()));
                    }
                });
        return null;
    }
}
