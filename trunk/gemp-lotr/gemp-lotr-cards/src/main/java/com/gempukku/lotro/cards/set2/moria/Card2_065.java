package com.gempukku.lotro.cards.set2.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: While bearing a Goblin Spear, this minion is damage +2.
 */
public class Card2_065 extends AbstractMinion {
    public Card2_065() {
        super(2, 6, 1, 4, Race.ORC, Culture.MORIA, "Goblin Spearman");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self,
                        Filters.and(
                                Filters.sameCard(self),
                                Filters.hasAttached(Filters.name("Goblin Spear"))
                        ), Keyword.DAMAGE, 2));
    }
}
