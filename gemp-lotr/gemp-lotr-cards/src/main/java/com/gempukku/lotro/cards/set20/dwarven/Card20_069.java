package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;

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
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new CantExertWithCardModifier(self, Race.DWARF,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return game.getGameState().getStackedCards(self).size()>=3;
                    }
                }, Side.SHADOW);
    }
}
