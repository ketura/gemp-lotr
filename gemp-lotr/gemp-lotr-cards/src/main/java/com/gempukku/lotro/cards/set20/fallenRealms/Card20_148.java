package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 1
 * Ways of the South
 * Fallen Realms	Condition • Support Area
 * Each Southron is ambush (1).
 */
public class Card20_148 extends AbstractPermanent {
    public Card20_148() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.FALLEN_REALMS, Zone.SUPPORT, "Ways of the South");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, Keyword.SOUTHRON, Keyword.AMBUSH, 1);
    }
}
