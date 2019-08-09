package com.gempukku.lotro.cards.set40.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Thorin III, Lord of the Longbeards
 * Set: Second Edition
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion - Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Card Number: 1R31
 * Game Text: Damage +1. While you can spot 3 [DWARVEN] cards stacked on a [DWARVEN] condition, Thorin III is strength +2.
 */
public class Card40_031 extends AbstractCompanion{
    public Card40_031() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Thorin III", "Lord of the Longbeards", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new SpotCondition(Culture.DWARVEN, CardType.CONDITION, Filters.hasStacked(3, Culture.DWARVEN)), 2);
        return Collections.singletonList(modifier);
    }
}
