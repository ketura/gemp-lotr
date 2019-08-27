package com.gempukku.lotro.cards.set7.dwarven;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.condition.InitiativeCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Damage +1. While you have initiative, Gimli is strength +2.
 */
public class Card7_006 extends AbstractCompanion {
    public Card7_006() {
        super(2, 6, 3, 6, Culture.DWARVEN, Race.DWARF, Signet.THEODEN, "Gimli", "Faithful Companion", true);
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new InitiativeCondition(Side.FREE_PEOPLE), 2));
    }
}
