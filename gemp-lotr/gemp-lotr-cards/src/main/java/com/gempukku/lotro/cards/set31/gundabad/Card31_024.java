package com.gempukku.lotro.cards.set31.gundabad;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.effects.AddBurdenEffect;
import com.gempukku.lotro.logic.effects.SelfExertEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.OptionalTriggerAction;
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
public class Card31_024 extends AbstractMinion {
    public Card31_024() {
        super(4, 9, 3, 3, Race.ORC, Culture.GUNDABAD, "Bolg", "Son of Azog", true);
			addKeyword(Keyword.DAMAGE, 1);
			addKeyword(Keyword.WARG_RIDER);
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