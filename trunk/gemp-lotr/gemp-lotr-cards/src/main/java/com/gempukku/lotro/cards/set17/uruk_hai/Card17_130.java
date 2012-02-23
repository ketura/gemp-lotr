package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 5
 * Vitality: 3
 * Site: 5
 * Game Text: Damage +1. Hunter 2. (While skirmishing a non-hunter character, this character is strength +2.)
 * The roaming penalty for each [URUK-HAI] minion you play is -1.
 */
public class Card17_130 extends AbstractMinion {
    public Card17_130() {
        super(2, 5, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "White Hand Scout");
        addKeyword(Keyword.DAMAGE, 1);
        addKeyword(Keyword.HUNTER, 2);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new RoamingPenaltyModifier(self, Filters.and(Culture.URUK_HAI, CardType.MINION, Filters.owner(self.getOwner())), -1);
    }
}
