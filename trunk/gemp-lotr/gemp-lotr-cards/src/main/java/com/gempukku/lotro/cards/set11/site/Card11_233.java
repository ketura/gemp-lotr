package com.gempukku.lotro.cards.set11.site;

import com.gempukku.lotro.cards.AbstractNewSite;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

/**
 * Set: Shadows
 * Twilight Cost: 2
 * Type: Site
 * Game Text: Underground. The Free Peoples player may transfer Free Peoples artifacts and possessions at no twilight
 * cost.
 */
public class Card11_233 extends AbstractNewSite {
    public Card11_233() {
        super("Chamber of Mazarbul", 2, Direction.LEFT);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new SpecialFlagModifier(self, ModifierFlag.TRANSFERS_FOR_FREE);
    }
}
