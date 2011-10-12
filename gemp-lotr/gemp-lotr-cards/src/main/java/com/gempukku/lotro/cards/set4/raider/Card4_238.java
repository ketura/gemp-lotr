package com.gempukku.lotro.cards.set4.raider;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DrawCardEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Raider
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Spot a site you control, spot 3 Southrons, and remove (2) to draw a card.
 */
public class Card4_238 extends AbstractPermanent {
    public Card4_238() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.RAIDER, Zone.SUPPORT, "Men of Harad");
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)
                && PlayConditions.canSpot(game, Filters.siteControlled(playerId))
                && PlayConditions.canSpot(game, 3, Filters.keyword(Keyword.SOUTHRON))) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
