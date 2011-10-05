package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.StrengthModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion • Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Gandalf
 * Game Text: While you can spot 3 [ELVEN] allies whose home is site 3, each minion skirmishing Arwen is strength -3.
 */
public class Card3_008 extends AbstractCompanion {
    public Card3_008() {
        super(2, 6, 3, Culture.ELVEN, Race.ELF, Signet.GANDALF, "Arwen", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.inSkirmishAgainst(Filters.sameCard(self)),
                        new SpotCondition(
                                Filters.and(
                                        Filters.culture(Culture.ELVEN),
                                        Filters.type(CardType.ALLY),
                                        Filters.siteNumber(3),
                                        Filters.siteBlock(Block.FELLOWSHIP)
                                ), 3), -3));
    }
}
