package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 1
 * Site: 5
 * Game Text: Damage +1. While the fellowship is at a forest site, the fellowship archery total is -1. Each [ELVEN]
 * companion skirmishing this minion is strength -2.
 */
public class Card13_165 extends AbstractMinion {
    public Card13_165() {
        super(3, 7, 1, 5, Race.URUK_HAI, Culture.URUK_HAI, "Isengard Infiltrator");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new ArcheryTotalModifier(self, Side.FREE_PEOPLE, new LocationCondition(Keyword.FOREST), -1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(CardType.COMPANION, Culture.ELVEN, Filters.inSkirmishAgainst(self)), -2));
        return modifiers;
    }
}
