package com.gempukku.lotro.cards.set19.gollum;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: Ages End
 * Side: Shadow
 * Culture: Gollum
 * Twilight Cost: 2
 * Type: Minion
 * Strength: 5
 * Vitality: 4
 * Site: 3
 * Game Text: Gollum is strength +1 for each threat you can spot. Each time Gollum wins a skirmish, you may add
 * a threat.
 */
public class Card19_010 extends AbstractMinion {
    public Card19_010() {
        super(2, 5, 4, 3, null, Culture.GOLLUM, "Gollum", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new ForEachThreatEvaluator());
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(playerId, self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
