package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Event • Maneuver
 * Game Text: Discard a condition for each [URUK-HAI] minion you can spot.
 */
public class Card17_137 extends AbstractEvent {
    public Card17_137() {
        super(Side.SHADOW, 2, Culture.URUK_HAI, "You Do Not Know Fear", Phase.MANEUVER);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        int count = Filters.countActive(game, Culture.URUK_HAI, CardType.MINION);
        action.appendEffect(
                new ChooseAndDiscardCardsFromPlayEffect(action, playerId, count, count, CardType.CONDITION));
        return action;
    }
}
