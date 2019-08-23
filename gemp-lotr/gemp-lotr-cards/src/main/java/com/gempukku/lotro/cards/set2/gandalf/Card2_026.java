package com.gempukku.lotro.cards.set2.gandalf;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.DrawCardsEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Fellowship or Regroup: Spot Gandalf to play the fellowship's next site (replacing opponent's site if
 * necessary). Draw a card if you play an underground site.
 */
public class Card2_026 extends AbstractEvent {
    public Card2_026() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Speak \"Friend\" and Enter", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return Filters.canSpot(game, Filters.gandalf);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, final LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlaySiteEffect(action, playerId, null, game.getGameState().getCurrentSiteNumber() + 1) {
                    @Override
                    protected void sitePlayedCallback(PhysicalCard site) {
                        if (game.getModifiersQuerying().hasKeyword(game, site, Keyword.UNDERGROUND))
                            action.appendEffect(
                                    new DrawCardsEffect(action, playerId, 1));
                    }
                });
        return action;
    }
}
