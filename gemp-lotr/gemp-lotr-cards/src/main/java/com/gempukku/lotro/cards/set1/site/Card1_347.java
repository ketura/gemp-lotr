package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
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
        super("Moria Stairway", Block.FELLOWSHIP, 4, 2, Direction.RIGHT);
        addKeyword(Keyword.UNDERGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self,
                Filters.and(
                        Filters.or(
                                CardType.COMPANION,
                                CardType.MINION),
                        Filters.hasAttached(
                                PossessionClass.HAND_WEAPON)
                ), Keyword.DAMAGE);
    }
}
