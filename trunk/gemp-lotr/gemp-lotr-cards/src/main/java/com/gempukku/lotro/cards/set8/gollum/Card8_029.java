package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Fellowship or Regroup
 * Game Text: Spot Smeagol to play the fellowship’s next site (replacing opponent’s site if necessary). The Shadow
 * number of the fellowship’s next site is -1 until the end of the turn.
 */
public class Card8_029 extends AbstractEvent {
    public Card8_029() {
        super(Side.FREE_PEOPLE, 0, Culture.GOLLUM, "Still Far Ahead", Phase.FELLOWSHIP, Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlaySiteEffect(playerId, null, game.getGameState().getCurrentSiteNumber() + 1));
        action.appendEffect(
                new AddUntilEndOfPhaseModifierEffect(
                        new TwilightCostModifier(self, Filters.siteNumber(game.getGameState().getCurrentSiteNumber() + 1), -1), game.getGameState().getCurrentPhase()));
        return action;
    }
}
