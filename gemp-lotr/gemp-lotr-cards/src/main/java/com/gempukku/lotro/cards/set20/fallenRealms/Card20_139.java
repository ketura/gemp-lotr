package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 5
 * Southron Packmaster
 * Minion • Man
 * 11	3	4
 * Southron. Archer.
 * While this minion is mounted, each Southron is ambush (1).
 * http://lotrtcg.org/coreset/fallenrealms/southronpackmaster(r1).png
 */
public class Card20_139 extends AbstractMinion {
    public Card20_139() {
        super(5, 10, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Packmaster");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new KeywordModifier(self, Keyword.SOUTHRON,
                new GameHasCondition(self, Filters.mounted), Keyword.AMBUSH, 1));
    }
}
