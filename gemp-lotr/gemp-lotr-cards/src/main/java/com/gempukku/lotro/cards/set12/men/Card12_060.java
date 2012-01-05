package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.FierceSkirmishCondition;
import com.gempukku.lotro.cards.modifiers.evaluator.CardMatchesEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 7
 * Vitality: 1
 * Site: 4
 * Game Text: Fierce. While this minion is in a fierce skirmish, it is strength +7 (or +10 if it is skirmishing
 * a companion who has resistance 4 or less).
 */
public class Card12_060 extends AbstractMinion {
    public Card12_060() {
        super(2, 7, 1, 4, Race.MAN, Culture.MEN, "Crazed Hillman");
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self,
                new FierceSkirmishCondition(),
                new CardMatchesEvaluator(7, 10, Filters.inSkirmishAgainst(CardType.COMPANION, Filters.maxResistance(4))));
    }
}
