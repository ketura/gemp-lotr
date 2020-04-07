package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 6
 * Type: Event • Shadow
 * Game Text: Toil 2. (For each [WRAITH] character you exert when playing this, its twilight cost is -2.) Spot a Nazgul
 * to draw 3 cards.
 */
public class Card11_207 extends AbstractEvent {
    public Card11_207() {
        super(Side.SHADOW, 6, Culture.WRAITH, "Dark Powers Strengthen", Phase.SHADOW);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new DrawCardsEffect(action, playerId, 3));
        return action;
    }
}
