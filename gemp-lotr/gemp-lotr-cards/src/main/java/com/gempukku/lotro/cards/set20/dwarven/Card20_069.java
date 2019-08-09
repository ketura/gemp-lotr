package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Staunch Defenders
 * Dwarven	Condition • Support Area
 * While you can spot 3 cards stacked on this condtion, Dwarves may not be exerted by shadow cards.
 */
public class Card20_069 extends AbstractPermanent {
    public Card20_069() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, "Staunch Defenders", null, true);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new CantExertWithCardModifier(self, Race.DWARF,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return game.getGameState().getStackedCards(self).size()>=3;
                    }
                }, Side.SHADOW));
    }
}
