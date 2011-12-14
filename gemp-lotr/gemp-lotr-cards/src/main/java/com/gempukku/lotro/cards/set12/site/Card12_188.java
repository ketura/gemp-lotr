package com.gempukku.lotro.cards.set12.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.ResistanceModifier;
import com.gempukku.lotro.cards.modifiers.conditions.OrCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

/**
 * Set: Black Rider
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Battleground. Forest. While you can spot 3 minions (or 6 companions), each unbound companion
 * is resistance -2.
 */
public class Card12_188 extends AbstractNewSite {
    public Card12_188() {
        super("Hill of Sight", 1, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new ResistanceModifier(self, Filters.unboundCompanion, new OrCondition(new SpotCondition(3, CardType.MINION), new SpotCondition(6, CardType.COMPANION)), -2);
    }
}
