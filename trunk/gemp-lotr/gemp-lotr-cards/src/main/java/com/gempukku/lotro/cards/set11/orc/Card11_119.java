package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
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
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 2
 * Site: 4
 * Game Text: Lurker. (Skirmishes involving lurker minions must be resolved after any others.) This minion is strength
 * +1 for each companion who is not assigned to a skirmish.
 */
public class Card11_119 extends AbstractMinion {
    public Card11_119() {
        super(4, 11, 2, 4, Race.ORC, Culture.ORC, "Emboldened Orc");
        addKeyword(Keyword.LURKER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null, new CountActiveEvaluator(CardType.COMPANION, Filters.notAssignedToSkirmish));
    }
}
