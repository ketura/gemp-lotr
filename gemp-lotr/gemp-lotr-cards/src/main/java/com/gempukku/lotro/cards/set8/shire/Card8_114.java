package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;

/**
 * Set: Siege of Gondor
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Event • Regroup
 * Game Text: Exert Frodo to remove (1) for each Frodo signet you spot.
 */
public class Card8_114 extends AbstractEvent {
    public Card8_114() {
        super(Side.FREE_PEOPLE, 0, Culture.SHIRE, "Straining Towards Us", Phase.REGROUP);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canExert(self, game, Filters.frodo);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendCost(
                new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Filters.frodo));
        action.appendEffect(
                new ForEachYouSpotEffect(playerId, CardType.COMPANION, Signet.FRODO) {
                    @Override
                    protected void spottedCards(int spotCount) {
                        action.appendEffect(
                                new RemoveTwilightEffect(spotCount));
                    }
                });
        return action;
    }
}
