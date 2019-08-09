package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Merry, Bucklander
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1C256
 * Game Text: While at a forest, Merry is strength +2.
 */
public class Card40_256 extends AbstractCompanion {
    public Card40_256() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Merry", "Bucklander", true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, self,
                new LocationCondition(Keyword.FOREST), 2);
        return Collections.singletonList(modifier);
    }
}
