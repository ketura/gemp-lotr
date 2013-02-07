package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 5
 * Threatening Olog-hai
 * Sauron	Minion • Troll
 * 14	4	6
 * Fierce.
 * This minion's twilight cost is +1 for each Free Peoples culture you can spot. Each time this minion wins a skirmish, add a threat.
 */
public class Card20_376 extends AbstractMinion {
    public Card20_376() {
        super(5, 14, 4, 6, Race.TROLL, Culture.SAURON, "Threatening Olog-hai");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        return GameUtils.getSpottableFPCulturesCount(gameState, modifiersQuerying, self.getOwner());
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.winsSkirmish(game, effectResult, self)) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddThreatsEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
