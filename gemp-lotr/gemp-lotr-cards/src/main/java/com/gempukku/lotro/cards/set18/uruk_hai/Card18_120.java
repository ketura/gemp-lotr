package com.gempukku.lotro.cards.set18.uruk_hai;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Uruk-Hai
 * Twilight Cost: 0
 * Type: Event • Maneuver
 * Game Text: Exert an [URUK-HAI] minion to reinforce an [URUK-HAI] token.
 */
public class Card18_120 extends AbstractEvent {
    public Card18_120() {
        super(Side.SHADOW, 0, Culture.URUK_HAI, "New Forges Built", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.URUK_HAI, CardType.MINION));
        action.appendEffect(
                new ReinforceTokenEffect(self, playerId, Token.URUK_HAI));
        return action;
    }
}
