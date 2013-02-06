package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;

/**
 * 1
 * Rohirrim Charge
 * Rohan	Event • Regroup
 * Wound a minion for each mounted [Rohan] man you can spot.
 */
public class Card20_337 extends AbstractEvent {
    public Card20_337() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Rohirrim Charge", Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, Culture.ROHAN, Race.MAN, Filters.mounted) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        for (int i=0; i<spotCount; i++)
                            action.appendEffect(
                                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, CardType.MINION));
                    }
                });
        return action;
    }
}
