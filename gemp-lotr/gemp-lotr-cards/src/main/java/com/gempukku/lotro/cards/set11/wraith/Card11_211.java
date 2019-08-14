package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 7
 * Type: Event • Maneuver
 * Game Text: Toil 2. (For each [WRAITH] character you exert when playing this, its twilight cost is -2.) Spot a Nazgul to add a burden.
 */
public class Card11_211 extends AbstractEvent {
    public Card11_211() {
        super(Side.SHADOW, 7, Culture.WRAITH, "Keening Wail", Phase.MANEUVER);
        addKeyword(Keyword.TOIL, 2);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddBurdenEffect(playerId, self, 1));
        return action;
    }
}
