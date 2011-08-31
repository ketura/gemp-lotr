package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 2
 * Type: Site
 * Site: 4
 * Game Text: Underground. Each companion and minion bearing a hand weapon is damage +1.
 */
public class Card1_347 extends AbstractSite {
    public Card1_347() {
        super("Moria Stairway", 4, 2, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new KeywordModifier(self,
                Filters.and(
                        Filters.or(
                                Filters.type(CardType.COMPANION),
                                Filters.type(CardType.MINION)),
                        Filters.hasAttached(
                                Filters.keyword(Keyword.HAND_WEAPON))
                ), Keyword.DAMAGE);
    }
}
