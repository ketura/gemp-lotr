package com.gempukku.lotro.cards.set12.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 12
 * Vitality: 3
 * Site: 5
 * Game Text: While this minion is at a battleground site, it is strength +2. While this minion is bearing a possession,
 * it is damage +1.
 */
public class Card12_150 extends AbstractMinion {
    public Card12_150() {
        super(4, 12, 3, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Decimator");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Keyword.BATTLEGROUND), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(self, Filters.hasAttached(CardType.POSSESSION)), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
