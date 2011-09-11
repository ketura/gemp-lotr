package com.gempukku.lotro.cards.set1.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ChooseAndExertCharacterEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
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
            DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.SHADOW, "Exert a SAURON Orc and remove (2) to draw a card.");
            action.addCost(
                    new ChooseAndExertCharacterEffect(action, playerId, "Choose a SAURON Orc", true, Filters.culture(Culture.SAURON), Filters.race(Race.ORC), Filters.canExert()));
            action.addCost(
                    new RemoveTwilightEffect(2));
            action.addEffect(
                    new DrawCardEffect(playerId));
            return Collections.singletonList(action);
        }
        return null;
    }
}
