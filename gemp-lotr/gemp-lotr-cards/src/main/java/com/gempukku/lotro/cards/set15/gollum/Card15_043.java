package com.gempukku.lotro.cards.set15.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfPhaseModifierEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 0
 * Vitality: 4
 * Site: 3
 * Game Text: At the start of each skirmish involving Gollum, he is strength +X until the end of that skirmish, where X
 * is the Ring-bearer’s strength.
 */
public class Card15_043 extends AbstractMinion {
    public Card15_043() {
        super(2, 0, 4, 3, null, Culture.GOLLUM, "Gollum", true);
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.startOfPhase(game, effectResult, Phase.SKIRMISH)
                && PlayConditions.canSpot(game, self, Filters.inSkirmish)) {
            int rbStr = game.getModifiersQuerying().getStrength(game.getGameState(),
                    game.getGameState().getRingBearer(game.getGameState().getCurrentPlayerId()));
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddUntilEndOfPhaseModifierEffect(
                            new StrengthModifier(self, self, rbStr), Phase.SKIRMISH));
            return Collections.singletonList(action);
        }
        return null;
    }
}
