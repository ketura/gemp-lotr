package com.gempukku.lotro.cards.set12.gandalf;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 1
 * Type: Event • Maneuver or Regroup
 * Game Text: Spot a [GANDALF] Wizard to replace a site in the fellowship's current region with a site from your
 * adventure deck.
 */
public class Card12_034 extends AbstractEvent {
    public Card12_034() {
        super(Side.FREE_PEOPLE, 1, Culture.GANDALF, "Traveled Leader", Phase.MANEUVER, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.GANDALF, Race.WIZARD);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);

        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose site to replace", CardType.SITE, Filters.region(GameUtils.getRegion(game.getGameState()))) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new PlaySiteEffect(action, playerId, null, card.getSiteNumber()));
                    }
                });
        return action;
    }
}
