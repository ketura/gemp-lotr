package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Ways of the South
 * Condition • Support Area
 * Each Southron is ambush (1).
 * http://lotrtcg.org/coreset/fallenrealms/waysofthesouth(r1).png
 */
public class Card20_148 extends AbstractPermanent {
    public Card20_148() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.FALLEN_REALMS, "Ways of the South", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new KeywordModifier(self, Keyword.SOUTHRON, Keyword.AMBUSH, 1));
}
}
