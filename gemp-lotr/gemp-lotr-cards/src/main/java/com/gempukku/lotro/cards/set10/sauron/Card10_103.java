package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.ChooseAndWoundCharactersEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot a [SAURON] minion. Each time the fellowship moves during the regroup phase, you may remove
 * a threat to wound an unbound companion.
 */
public class Card10_103 extends AbstractPermanent {
    public Card10_103() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Window of the Eye");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.WHEN_FELLOWSHIP_MOVES
                && PlayConditions.isPhase(game, Phase.REGROUP)
                && PlayConditions.canRemoveThreat(game, self, 1)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendCost(
                    new RemoveThreatsEffect(self, 1));
            action.appendEffect(
                    new ChooseAndWoundCharactersEffect(action, playerId, 1, 1, Filters.unboundCompanion));
            return Collections.singletonList(action);
        }
        return null;
    }
}
