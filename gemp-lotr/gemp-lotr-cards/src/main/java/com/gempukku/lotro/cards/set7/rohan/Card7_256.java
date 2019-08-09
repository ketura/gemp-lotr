package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.timing.UnrespondableEffect;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Regroup
 * Game Text: Exert X mounted companions to wound X minions.
 */
public class Card7_256 extends AbstractEvent {
    public Card7_256() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "They Sang as They Slew", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        final AtomicInteger exertCount = new AtomicInteger(0);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 0, Integer.MAX_VALUE, CardType.COMPANION, Filters.mounted) {
                    @Override
                    protected void forEachCardExertedCallback(PhysicalCard character) {
                        exertCount.incrementAndGet();
                    }
                });
        action.appendEffect(
                new UnrespondableEffect() {
                    @Override
                    protected void doPlayEffect(LotroGame game) {
                        action.appendEffect(
                                new ChooseAndWoundCharactersEffect(action, playerId, exertCount.get(), exertCount.get(), CardType.MINION));
                    }
                });

        return action;
    }
}
