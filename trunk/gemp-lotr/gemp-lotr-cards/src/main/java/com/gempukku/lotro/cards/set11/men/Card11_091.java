package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.effects.DiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Condition • Support Area
 * Game Text: At the start of the maneuver phase, if you have 2 or more cards in hand, you may spot 2 [MEN] minions
 * and discard your hand to add (8).
 */
public class Card11_091 extends AbstractPermanent {
    public Card11_091() {
        super(Side.SHADOW, 4, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Oath Sworn");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && game.getGameState().getHand(playerId).size() >= 2
                && PlayConditions.canSpot(game, 2, Culture.MEN, CardType.MINION)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new DiscardCardsFromHandEffect(self, playerId, new HashSet<PhysicalCard>(game.getGameState().getHand(playerId)), false));
            action.appendEffect(
                    new AddTwilightEffect(self, 8));
            return Collections.singletonList(action);
        }
        return null;
    }
}
