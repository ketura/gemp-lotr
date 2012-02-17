package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.MultiplyEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 7
 * Type: Minion • Uruk-Hai
 * Strength: 13
 * Vitality: 3
 * Site: 5
 * Game Text: Archer. Damage +1. Muster. (At the start of the regroup phase, you may discard a card from hand to draw
 * a card.) Lurtz is strength +3 for each exhausted companion you can spot.
 */
public class Card11_194 extends AbstractMinion {
    public Card11_194() {
        super(7, 13, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Lurtz", true);
        addKeyword(Keyword.ARCHER);
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.MUSTER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new MultiplyEvaluator(3, new CountActiveEvaluator(CardType.COMPANION, Filters.exhausted)));
    }
}
