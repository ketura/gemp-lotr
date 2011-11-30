package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.cards.modifiers.evaluator.NegativeEvaluator;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 7
 * Game Text: Each minion skirmishing Arwen is strength -1 for each forest site on the adventure path.
 */
public class Card11_015 extends AbstractCompanion {
    public Card11_015() {
        super(2, 6, 3, 7, Culture.ELVEN, Race.ELF, null, "Arwen", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(self)), null,
                        new NegativeEvaluator(
                                new CountActiveEvaluator(CardType.SITE, Zone.ADVENTURE_PATH, Keyword.FOREST))));
    }
}
