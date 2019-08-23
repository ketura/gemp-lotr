package com.gempukku.lotro.cards.set40.gondor;

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
 * Title: Pathfinder
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Event - Fellowship or Regroup
 * Card Number: 1C113
 * Game Text: Spot a ranger to play the fellowship's next site (replacing opponent's site if necessary).
 */
public class Card40_113 extends AbstractEvent {
    public Card40_113() {
        super(Side.FREE_PEOPLE, 0, Culture.GONDOR, "Pathfinder", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self, true);
        action.appendEffect(new PlayNextSiteEffect(action, playerId));
        return action;
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Keyword.RANGER);
    }
}
