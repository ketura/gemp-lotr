package com.gempukku.lotro.cards.set8.gollum;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.logic.effects.PlayNextSiteEffect;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.smeagol);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new PlayNextSiteEffect(action, playerId));
        action.appendEffect(
                new AddUntilEndOfTurnModifierEffect(
                        new TwilightCostModifier(self, Filters.and(CardType.SITE, Filters.siteNumber(game.getGameState().getCurrentSiteNumber() + 1)), -1)));
        return action;
    }
}
