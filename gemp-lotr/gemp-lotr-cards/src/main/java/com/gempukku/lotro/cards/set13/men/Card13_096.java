package com.gempukku.lotro.cards.set13.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 8
 * Vitality: 2
 * Site: 4
 * Game Text: While you can spot another [MEN] minion, this minion is strength +2 for each companion assigned to
 * a skirmish.
 */
public class Card13_096 extends AbstractMinion {
    public Card13_096() {
        super(3, 8, 2, 4, Race.MAN, Culture.MEN, "Merciless Dunlending");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self,
                new SpotCondition(Filters.not(self), Culture.MEN, CardType.MINION),
                new MultiplyEvaluator(2, new CountActiveEvaluator(CardType.COMPANION, Filters.assignedToSkirmish)));
    }
}
