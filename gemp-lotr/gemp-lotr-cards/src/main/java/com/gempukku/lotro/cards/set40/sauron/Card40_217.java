package com.gempukku.lotro.cards.set40.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.MinThreatCondition;
import com.gempukku.lotro.logic.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.NegativeEvaluator;

import java.util.Arrays;
import java.util.List;

/**
 * Title: Fires Raged Unchecked
 * Set: Second Edition
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition - Support Area
 * Card Number: 1R217
 * Game Text: The site number of each [SAURON] Orc is -1 for each threat.
 * While you can spot 3 threats, each [SAURON] Orc that is not roaming is strength +1.
 */
public class Card40_217 extends AbstractPermanent {
    public Card40_217() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, "Fires Raged Unchecked");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        MinionSiteNumberModifier siteNumber = new MinionSiteNumberModifier(self, Filters.and(Culture.SAURON, Race.ORC), null,
                new NegativeEvaluator(new ForEachThreatEvaluator()));
        StrengthModifier strength = new StrengthModifier(self, Filters.and(Culture.SAURON, Race.ORC, Filters.not(Filters.roamingMinion)),
                new MinThreatCondition(3), 1);
        return Arrays.asList(siteNumber, strength);
    }
}
