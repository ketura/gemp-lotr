package com.gempukku.lotro.cards.set8.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.ForEachYouSpotEffect;
import com.gempukku.lotro.logic.effects.RemoveTwilightEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canExert(self, game, Filters.frodo);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
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
