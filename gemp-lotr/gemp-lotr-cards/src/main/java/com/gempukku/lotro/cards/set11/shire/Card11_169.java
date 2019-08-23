package com.gempukku.lotro.cards.set11.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.RemoveBurdenEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 2, Culture.SHIRE, CardType.COMPANION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new RemoveBurdenEffect(playerId, self, 2));
        return action;
    }
}
