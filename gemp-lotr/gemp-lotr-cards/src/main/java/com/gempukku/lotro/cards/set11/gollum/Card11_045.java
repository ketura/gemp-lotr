package com.gempukku.lotro.cards.set11.gollum;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.ChooseActiveCardEffect;
import com.gempukku.lotro.logic.effects.PlaySiteEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 0
 * Type: Event • Shadow
 * Game Text: Spot Gollum to add (3) and exchange one of your sites on the adventure path with another site from your
 * adventure deck.
 */
public class Card11_045 extends AbstractEvent {
    public Card11_045() {
        super(Side.SHADOW, 0, Culture.GOLLUM, "Led Astray", Phase.SHADOW);
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Filters.gollum);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(final String playerId, LotroGame game, PhysicalCard self) {
        final PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddTwilightEffect(self, 3));
        action.appendEffect(
                new ChooseActiveCardEffect(self, playerId, "Choose your site", Zone.ADVENTURE_PATH, CardType.SITE, Filters.owner(playerId)) {
                    @Override
                    protected void cardSelected(LotroGame game, PhysicalCard card) {
                        action.appendEffect(
                                new PlaySiteEffect(action, playerId, null, card.getSiteNumber()));
                    }
                });
        return action;
    }
}
