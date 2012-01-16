package com.gempukku.lotro.cards.set13.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;
import com.gempukku.lotro.logic.modifiers.SpecialFlagModifier;

/**
 * Set: Bloodlines
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Mountain. Culture tokens cannot be added, removed, or reinforced.
 */
public class Card13_194 extends AbstractNewSite {
    public Card13_194() {
        super("Redhorn Pass", 2, Direction.LEFT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new SpecialFlagModifier(self, ModifierFlag.CANT_TOUCH_CULTURE_TOKENS);
    }
}
