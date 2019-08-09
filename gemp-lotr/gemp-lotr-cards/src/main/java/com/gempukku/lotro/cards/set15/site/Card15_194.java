package com.gempukku.lotro.cards.set15.site;

import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: The Hunters
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Plains. Each hunter character is strength +1.
 */
public class Card15_194 extends AbstractShadowsSite {
    public Card15_194() {
        super("Westfold Village", 2, Direction.LEFT);
        addKeyword(Keyword.PLAINS);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.character, Keyword.HUNTER), 1);
    }
}
