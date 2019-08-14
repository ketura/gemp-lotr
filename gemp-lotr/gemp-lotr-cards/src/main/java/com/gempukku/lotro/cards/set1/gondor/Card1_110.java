package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.PlayNextSiteEffect;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Fellowship or Regroup: Spot a ranger to play the fellowship's next site (replacing opponent's site if
 * necessary).
 */
public class Card1_110 extends AbstractEvent {
    public Card1_110() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Pathfinder", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self, true);
        action.appendEffect(new PlayNextSiteEffect(action, playerId));
        return action;
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Keyword.RANGER);
    }
}
