package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
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
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 6
 * Vitality: 2
 * Site: 5
 * Game Text: Tracker. Fierce. While skirmishing a character bearing a search card, this minion is strength +2
 * and damage +1.
 */
public class Card4_195 extends AbstractMinion {
    public Card4_195() {
        super(2, 6, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Seeker");
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.sameCard(self), Filters.inSkirmishAgainst(Filters.hasAttached(Filters.keyword(Keyword.SEARCH)))), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.sameCard(self), Filters.inSkirmishAgainst(Filters.hasAttached(Filters.keyword(Keyword.SEARCH)))), Keyword.DAMAGE));
        return modifiers;
    }
}
