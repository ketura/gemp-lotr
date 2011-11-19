package com.gempukku.lotro.cards.set9.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Reflections
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 4
 * Type: Companion • Dwarf
 * Strength: 7
 * Vitality: 4
 * Resistance: 6
 * Game Text: Damage +1. While you can spot 2 Dwarves, Durin III is twilight cost -2. Durin III is strength +1 for each
 * artifact and each possession he bears.
 */
public class Card9_003 extends AbstractCompanion {
    public Card9_003() {
        super(4, 7, 4, Culture.DWARVEN, Race.DWARF, null, "Durin III", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public int getTwilightCostModifier(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
        if (Filters.countSpottable(gameState, modifiersQuerying, Race.DWARF) >= 2)
            return -2;
        return 0;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.attachedTo(self), Filters.or(CardType.POSSESSION, CardType.ARTIFACT))));
    }
}
