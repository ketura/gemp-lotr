package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Shadow: Play a [MORIA] minion from your discard pile.
 */
public class Card2_059 extends AbstractEvent {
    public Card2_059() {
        super(Side.SHADOW, 2, Culture.MORIA, "Foul Things", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canPlayFromDiscard(self.getOwner(), game, Culture.MORIA, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.MORIA, CardType.MINION));

        return action;
    }
}
