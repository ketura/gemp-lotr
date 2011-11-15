package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Event
 * Game Text: Shadow: Play a [MORIA] minion from your discard pile.
 */
public class Card2_059 extends AbstractOldEvent {
    public Card2_059() {
        super(Side.SHADOW, Culture.MORIA, "Foul Things", Phase.SHADOW);
    }

    @Override
    public int getTwilightCost() {
        return 2;
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                // There has to be playable MORIA Orc in discard pile to be able to use "Foul Things"
                && Filters.filter(game.getGameState().getDiscard(playerId), game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.MORIA), CardType.MINION, Filters.playable(game)).size() > 0;
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game.getGameState().getDiscard(playerId), Filters.culture(Culture.MORIA)));

        return action;
    }
}
