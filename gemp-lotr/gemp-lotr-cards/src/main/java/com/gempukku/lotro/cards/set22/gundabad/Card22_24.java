package com.gempukku.lotro.cards.set22.gundabad;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Gundabad
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 9
 * Vitality: 3
 * Site: 3
 * Game Text: Each times Bolg wins a skirmish, you may exert him twice to add a doubt.
 */
public class Card22_24 extends AbstractMinion {
    public Card22_24() {
        super(4, 9, 3, 3, null, Culture.GUNDABAD, "Bolg", "Son of Azog", true);
		addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<OptionalTriggerAction> getOptionalAfterTriggers(String playerId, LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)
				&& PlayConditions.canExert(self, game, 2, self)) {
            OptionalTriggerAction action = new OptionalTriggerAction(self);
			action.appendCost(new SelfExertEffect(action, self));
			action.appendCost(new SelfExertEffect(action, self));
            action.appendEffect(
					new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
