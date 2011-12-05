package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 6
 * Type: Event • Fellowship
 * Game Text: Toil 2. (For each [SHIRE] character you exert when playing this, its twilight cost is -2.) Spot 2 [SHIRE]
 * companions to remove 2 burdens.
 */
public class Card11_169 extends AbstractEvent {
    public Card11_169() {
        super(Side.FREE_PEOPLE, 6, Culture.SHIRE, "The More, The Merrier", Phase.FELLOWSHIP);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, 2));
        return action;
    }
}
