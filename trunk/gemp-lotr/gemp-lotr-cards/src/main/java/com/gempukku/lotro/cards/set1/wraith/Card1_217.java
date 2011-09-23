package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Shadow: Play a Nazgul. His twilight cost is -2.
 */
public class Card1_217 extends AbstractEvent {
    public Card1_217() {
        super(Side.SHADOW, Culture.WRAITH, "Morgul Gates", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.filter(game.getGameState().getHand(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.NAZGUL), Filters.playable(game, -2)).size() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new ChooseAndPlayCardFromHandEffect(playerId, game.getGameState().getHand(playerId), Filters.race(Race.NAZGUL), -2));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }
}
