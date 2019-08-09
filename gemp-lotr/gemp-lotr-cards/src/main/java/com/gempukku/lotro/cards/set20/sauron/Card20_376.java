package com.gempukku.lotro.cards.set20.sauron;
import java.util.List;
import java.util.Collections;
import java.util.List;
import java.util.Collections;import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.timing.TriggerConditions;
import com.gempukku.lotro.logic.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Threatening Olog-hai
 * Minion • Troll
 * 14	3	6
 * Fierce.
 * This minion is strength -1 for each Free Peoples culture you can spot.
 * Each time this minion wins a skirmish, add a threat.
 * http://lotrtcg.org/coreset/sauron/threateningologhai(r1).png
 */
public class Card20_376 extends AbstractMinion {
    public Card20_376() {
        super(4, 14, 3, 6, Race.TROLL, Culture.SAURON, "Threatening Olog-hai");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new NegativeEvaluator(new CountFPCulturesEvaluator(self.getOwner()))));
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
