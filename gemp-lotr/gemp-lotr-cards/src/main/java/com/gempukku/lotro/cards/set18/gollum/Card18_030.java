package com.gempukku.lotro.cards.set18.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.ReinforceTokenEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Heal a Ring-bound companion to reinforce a [GOLLUM] token.
 */
public class Card18_030 extends AbstractEvent {
    public Card18_030() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "Enemy in Your Midst", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canHeal(self, game, CardType.COMPANION, Keyword.RING_BOUND);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndHealCharactersEffect(action, playerId, CardType.COMPANION, Keyword.RING_BOUND));
        action.appendEffect(
                new ReinforceTokenEffect(self, playerId, Token.GOLLUM));
        return action;
    }
}
