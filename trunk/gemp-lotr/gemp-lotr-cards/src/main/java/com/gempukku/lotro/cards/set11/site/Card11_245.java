package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Battleground. Each character bearing a hand weapon is strength +2.
 */
public class Card11_245 extends AbstractNewSite {
    public Card11_245() {
        super("Helm's Gate", 2, Direction.LEFT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, Filters.and(Filters.character, Filters.hasAttached(PossessionClass.HAND_WEAPON)), 2);
    }
}
