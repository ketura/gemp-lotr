package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Play a Nazgul. His twilight cost is -1 for each forest site you can spot (limit -4).
 */
public class Card12_162 extends AbstractEvent {
    public Card12_162() {
        super(Side.SHADOW, 0, Culture.WRAITH, "Dark Approach", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        int modifier = Math.max(-4, -Filters.countActive(game, CardType.SITE, Keyword.FOREST));
        return PlayConditions.canPlayFromHand(self.getOwner(), game, modifier, Race.NAZGUL);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        int modifier = Math.max(-4, -Filters.countActive(game, CardType.SITE, Keyword.FOREST));
        action.appendEffect(
                new ChooseAndPlayCardFromHandEffect(playerId, game, modifier, Race.NAZGUL));
        return action;
    }
}
