package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.TriggerConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.RequiredTriggerAction;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * •Troll of Morannon
 * Minion • Troll
 * 14	3	6
 * Damage +1. Fierce.
 * This minion is strength -1 for each Free Peoples culture you can spot.
 * When you play this minion in region 3, you may spot a [Sauron] or [Ringwraith] minion to add a burden.
 * http://lotrtcg.org/coreset/sauron/trollofmorannon(r1).png
 */
public class Card20_378 extends AbstractMinion {
    public Card20_378() {
        super(4, 12, 3, 6, Race.TROLL, Culture.SAURON, "Troll of Morannon", null, true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, null, new NegativeEvaluator(new CountFPCulturesEvaluator(self.getOwner())));
    }

    @Override
    public List<RequiredTriggerAction> getRequiredAfterTriggers(LotroGame game, EffectResult effectResult, PhysicalCard self) {
        if (TriggerConditions.played(game, effectResult, self)
                && PlayConditions.location(game, Filters.region(3))
                && PlayConditions.canSpot(game, CardType.MINION, Filters.or(Culture.SAURON, Culture.WRAITH))) {
            RequiredTriggerAction action = new RequiredTriggerAction(self);
            action.appendEffect(
                    new AddBurdenEffect(self.getOwner(), self, 1));
            return Collections.singletonList(action);
        }
        return null;
    }
}
