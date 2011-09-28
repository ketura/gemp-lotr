package com.gempukku.lotro.cards.set2.moria;

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

import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 6
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 3
 * Site: 4
 * Game Text: Archer. While you can spot another [MORIA] archer, add 1 to the minion archery total.
 */
public class Card2_067 extends AbstractMinion {
    public Card2_067() {
        super(6, 8, 3, 4, Race.ORC, Culture.MORIA, "Moria Archer Troop");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW,
                        new SpotCondition(Filters.and(Filters.not(Filters.sameCard(self)), Filters.keyword(Keyword.ARCHER))), 1));
    }
}
