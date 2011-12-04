package com.gempukku.lotro.cards.set11.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.filters.Filters;

import java.util.List;
import java.util.LinkedList;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 4
 * Type: Companion • Wizard
 * Strength: 7
 * Vitality: 4
 * Resistance: 7
 * Game Text: While Gandalf is in region 1, each other companion is strength +2. While Gandalf is in region 2, each
 * companion is strength +1. While Gandalf is in region 3, he is strength +2.
 */
public class Card11_033 extends AbstractCompanion {
    public Card11_033() {
        super(4, 7, 4, 7, Culture.GANDALF, Race.WIZARD, null, "Gandalf", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(CardType.COMPANION, Filters.not(self)), new LocationCondition(Filters.region(1)), 2));
        modifiers.add(
                new StrengthModifier(self, CardType.COMPANION, new LocationCondition(Filters.region(2)), 1));
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Filters.region(3)), 2));
        return modifiers;
    }
}
