package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Fires Raged Unchecked
 * Sauron	Condition • Support Area
 * The site number of each [Sauron] Orc is -1 for each threat.
 * While you can spot 3 threats, each [Sauron] Orc that is not roaming is strength + 1.
 */
public class Card20_381 extends AbstractPermanent {
    public Card20_381() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Fires Raged Unchecked");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MinionSiteNumberModifier(self, Filters.and(Culture.SAURON, Race.ORC), null, new MultiplyEvaluator(-1, new ForEachThreatEvaluator())));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.SAURON, Race.ORC, Filters.not(Keyword.ROAMING)), new MinThreatCondition(3), 1));
        return modifiers;
    }
}
