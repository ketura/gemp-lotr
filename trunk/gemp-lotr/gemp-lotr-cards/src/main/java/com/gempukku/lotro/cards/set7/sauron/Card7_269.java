package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.MinionSiteNumberModifier;
import com.gempukku.lotro.cards.modifiers.conditions.MinThreatCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.ForEachThreatEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: The site number of each [SAURON] Orc is -1 for each threat. While you can spot 3 threats, each
 * [SAURON] Orc that is not roaming is strength +1
 */
public class Card7_269 extends AbstractPermanent {
    public Card7_269() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.SAURON, Zone.SUPPORT, "Fires Raged Unchecked");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new MinionSiteNumberModifier(self, Filters.and(Culture.SAURON, Race.ORC), null, new NegativeEvaluator(new ForEachThreatEvaluator())));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Culture.SAURON, Race.ORC, Filters.not(Keyword.ROAMING)), new MinThreatCondition(3), 1));
        return modifiers;
    }
}
