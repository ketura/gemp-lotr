package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 4
 * Vitality: 3
 * Site: 4
 * Game Text: Archer. While you can spot another [MORIA] Orc, the fellowship archery total is -6.
 */
public class Card1_172 extends AbstractMinion {
    public Card1_172() {
        super(5, 4, 3, 4, Race.ORC, Culture.MORIA, "Goblin Archer");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new SpotCondition(Culture.MORIA, Race.ORC, Filters.not(self)), -6);
    }
}
