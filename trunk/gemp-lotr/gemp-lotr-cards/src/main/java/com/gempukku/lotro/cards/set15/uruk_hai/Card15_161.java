package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 2
 * Site: 5
 * Game Text: While this minion is at a battleground site, he gains hunter 1 (While skirmishing a non-hunter character,
 * this character is strength +1.)
 */
public class Card15_161 extends AbstractMinion {
    public Card15_161() {
        super(4, 12, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Hunting Uruk");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new KeywordModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), Keyword.HUNTER, 1);
    }
}
