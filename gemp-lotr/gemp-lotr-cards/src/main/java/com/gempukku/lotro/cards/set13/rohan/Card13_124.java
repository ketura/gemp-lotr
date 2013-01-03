package com.gempukku.lotro.cards.set13.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.ReinforceTokenEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.condition.PhaseCondition;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Each possession you play on Eowyn during the fellowship phase is twilight cost -1. Each time
 * the fellowship moves during the regroup phase, you may reinforce a [ROHAN] token (or reinforce 2 [ROHAN] tokens
 * if you can spot Eomer or Theoden).
 */
public class Card13_124 extends AbstractCompanion {
    public Card13_124() {
        super(2, 6, 3, 6, Culture.ROHAN, Race.MAN, null, Names.eowyn, "Restless Warrior", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new AbstractModifier(self, "The cost of each possession you play on Eowyn during the fellowship phase is twilight cost -1", CardType.POSSESSION, new PhaseCondition(Phase.FELLOWSHIP), ModifierEffect.TWILIGHT_COST_MODIFIER) {
            @Override
            public int getPlayOnTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target) {
                if (target == self)
                    return -1;
                return 0;
            }
        };
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.moves(game, effectResult)
                && PlayConditions.isPhase(game, Phase.REGROUP)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            int count = PlayConditions.canSpot(game, Filters.or(Filters.name(Names.eomer), Filters.name(Names.theoden))) ? 2 : 1;
            action.appendEffect(
                    new ReinforceTokenEffect(self, playerId, Token.ROHAN, count));
            return Collections.singletonList(action);
        }
        return null;
    }
}
