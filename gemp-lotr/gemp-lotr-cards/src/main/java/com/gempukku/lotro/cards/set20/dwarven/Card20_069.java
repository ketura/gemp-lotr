package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * 2
 * •Staunch Defenders
 * Dwarven	Condition • Support Area
 * While you can spot 3 cards stacked on this condtion, Dwarves may not be exerted by shadow cards.
 */
public class Card20_069 extends AbstractPermanent {
    public Card20_069() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.SUPPORT, "Staunch Defenders", null, true);
    }

    @Override
    public Modifier getAlwaysOnModifier(final PhysicalCard self) {
        return new CantExertWithCardModifier(self, Race.DWARF,
                new Condition() {
                    @Override
                    public boolean isFullfilled(GameState gameState, ModifiersQuerying modifiersQuerying) {
                        return gameState.getStackedCards(self).size()>=3;
                    }
                }, Side.SHADOW);
    }
}
