package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1(or damage +2 while you control a site).
 */
public class Card4_206 extends AbstractMinion {
    public Card4_206() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk-hai Patrol");
        addKeyword(Keyword.DAMAGE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.sameCard(self), new SpotCondition(Filters.siteControlled(self.getOwner())), Keyword.DAMAGE, 1));
    }
}
