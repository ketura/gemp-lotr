package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Arwen, Fearless Rider
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion - Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 8
 * Card Number: 1C34
 * Game Text: Ranger. While at a river or forest, Arwen is strength +3.
 */
public class Card40_034 extends AbstractCompanion{
    public Card40_034() {
        super(2, 6, 3, 8, Culture.ELVEN, Race.ELF, null, "Arwen",
                "Fearless Rider", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new LocationCondition(Filters.or(Keyword.RIVER, Keyword.FOREST)), 3);
        return Collections.singletonList(modifier);
    }
}
