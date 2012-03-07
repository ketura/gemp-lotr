package com.gempukku.lotro.cards.set18.men;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 4
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 2
 * Game Text: While the fellowship is in region 1, this minion is strength +4. While the fellowship is in region 2,
 * this minion is strength +2.
 */
public class Card18_074 extends AbstractMinion {
    public Card18_074() {
        super(4, 9, 2, 2, Race.MAN, Culture.MEN, "Squint-eyed Southerner");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Filters.region(1)), 4));
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Filters.region(2)), 2));
        return modifiers;
    }
}
