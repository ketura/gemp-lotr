package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 10
 * Vitality: 3
 * Site: 5
 * Game Text: Fierce. This minion gains hunter 3 for each site you control.
 */
public class Card17_124 extends AbstractMinion {
    public Card17_124() {
        super(5, 10, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Destroyer", true);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self, new SpotCondition(Filters.siteControlled(self.getOwner())), Keyword.HUNTER,
                new MultiplyEvaluator(3, new CountActiveEvaluator(Filters.siteControlled(self.getOwner()))));
    }
}
