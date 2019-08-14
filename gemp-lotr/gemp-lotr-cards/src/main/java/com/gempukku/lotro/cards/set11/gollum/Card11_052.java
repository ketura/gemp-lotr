package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Spot a [GOLLUM] minion at a mountain site to play a minion from your discard pile.
 */
public class Card11_052 extends AbstractEvent {
    public Card11_052() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "Strange and Terrible", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.GOLLUM, CardType.MINION)
                && PlayConditions.location(game, Keyword.MOUNTAIN)
                && PlayConditions.canPlayFromDiscard(self.getOwner(), game, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, CardType.MINION));
        return action;
    }
}
