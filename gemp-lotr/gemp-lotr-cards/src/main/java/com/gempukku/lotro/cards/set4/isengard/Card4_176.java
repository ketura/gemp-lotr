package com.gempukku.lotro.cards.set4.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.RoamingPenaltyModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 3
 * Site: 5
 * Game Text: Tracker. Fierce. The roaming penalty for each [ISENGARD] tracker you play is -2. While you can spot
 * 2 [ISENGARD] trackers, Ugluk is strength +3. While you can spot 3 [ISENGARD] trackers, Ugluk is damage +1.
 */
public class Card4_176 extends AbstractMinion {
    public Card4_176() {
        super(4, 9, 3, 5, Race.URUK_HAI, Culture.ISENGARD, "Ugluk", true);
        addKeyword(Keyword.TRACKER);
        addKeyword(Keyword.FIERCE);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new RoamingPenaltyModifier(self, Filters.and(Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER)), -2));
        modifiers.add(
                new StrengthModifier(self, Filters.sameCard(self), new SpotCondition(2, Filters.and(Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER))), 3));
        modifiers.add(
                new KeywordModifier(self, Filters.sameCard(self), new SpotCondition(3, Filters.and(Filters.culture(Culture.ISENGARD), Filters.keyword(Keyword.TRACKER))), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
