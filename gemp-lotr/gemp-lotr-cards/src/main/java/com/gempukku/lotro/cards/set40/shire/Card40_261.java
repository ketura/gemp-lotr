package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Arrays;
import java.util.List;

/**
 * Title: *Pippin, Frodo's Cousin
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion - Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 8
 * Card Number: 1C261
 * Game Text: Each companion bearing a pipe is strength +1. While Pippin bears a pipe, he is strength +1.
 */
public class Card40_261 extends AbstractCompanion {
    public Card40_261() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Pippin",
                "Frodo's Cousin", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        StrengthModifier modifier1 = new StrengthModifier(self, Filters.and(CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)), 1);
        StrengthModifier modifier2= new StrengthModifier(self, Filters.and(self, Filters.hasAttached(PossessionClass.PIPE)), 1);
        return Arrays.asList(modifier1, modifier2);
    }
}
