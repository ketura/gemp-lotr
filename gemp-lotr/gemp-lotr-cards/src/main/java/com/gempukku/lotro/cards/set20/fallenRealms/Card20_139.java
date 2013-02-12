package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.GameHasCondition;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 6
 * Southron Packmaster
 * Fallen Realms	Minion • Man
 * 13	3	4
 * Southron. Archer.
 * While this minion is mounted, each Southron is ambush (2).
 */
public class Card20_139 extends AbstractMinion {
    public Card20_139() {
        super(6, 13, 3, 4, Race.MAN, Culture.FALLEN_REALMS, "Southron Packmaster");
        addKeyword(Keyword.SOUTHRON);
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new KeywordModifier(self, Keyword.SOUTHRON,
                new GameHasCondition(self, Filters.mounted), Keyword.AMBUSH, 2);
    }
}
