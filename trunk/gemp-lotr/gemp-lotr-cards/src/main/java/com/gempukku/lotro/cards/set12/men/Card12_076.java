package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilStartOfPhaseModifierEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 0
 * Type: Condition • Support Area
 * Game Text: At the start of the maneuver phase, you may spot 3 [MEN] minions and remove (1) to make each unbound
 * companion resistance -1 until the regroup phase.
 */
public class Card12_076 extends AbstractPermanent {
    public Card12_076() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.MEN, Zone.SUPPORT, "Trail of Terror");
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.MANEUVER)
                && PlayConditions.canSpot(game, 3, Culture.MEN, CardType.MINION)
                && game.getGameState().getTwilightPool() >= 1) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveTwilightEffect(1));
            action.appendEffect(
                    new AddUntilStartOfPhaseModifierEffect(
                            new ResistanceModifier(self, Filters.unboundCompanion, -1), Phase.REGROUP));
            return Collections.singletonList(action);
        }
        return null;
    }
}
