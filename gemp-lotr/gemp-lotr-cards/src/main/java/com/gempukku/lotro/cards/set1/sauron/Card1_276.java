package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndExertCharactersCost;
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
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. Shadow: Exert a [SAURON] Orc and remove (2) to draw a card.
 */
public class Card1_276 extends AbstractPermanent {
    public Card1_276() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "Seeking Its Master");
    }

    @Override
    public List<? extends Action> getExtraPhaseActions(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game.getGameState(), Phase.SHADOW, self, 2)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert())) {
            ActivateCardAction action = new ActivateCardAction(self, Keyword.SHADOW, "Exert a SAURON Orc and remove (2) to draw a card.");
            action.appendCost(
                    new ChooseAndExertCharactersCost(action, playerId, 1, 1, Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert()));
            action.appendCost(
                    new RemoveTwilightEffect(2));
            action.appendEffect(
                    new DrawCardEffect(playerId, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
