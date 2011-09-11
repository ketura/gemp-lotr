package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Condition
 * Game Text: Plays to your support area. Each Dwarf is damage +1.
 */
public class Card1_021 extends AbstractPermanent {
    public Card1_021() {
        super(Side.FREE_PEOPLE, 2, CardType.CONDITION, Culture.DWARVEN, Zone.FREE_SUPPORT, "Lord of Moria");
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new KeywordModifier(self, Filters.race(Race.DWARF), Keyword.DAMAGE);
    }
}
