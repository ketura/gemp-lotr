package com.gempukku.lotro.cards.set11.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Names;
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
 * Set: Shadows
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Nazgul
 * Strength: 10
 * Vitality: 3
 * Site: 3
 * Game Text: While Úlairë Cantëa is at a forest or dwelling site, he is strength +2 and fierce.
 */
public class Card11_220 extends AbstractMinion {
    public Card11_220() {
        super(5, 10, 3, 3, Race.NAZGUL, Culture.WRAITH, Names.cantea, "Fourth of the Nine Riders", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new LocationCondition(Filters.or(Keyword.FOREST, Keyword.DWELLING)), 2));
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Filters.or(Keyword.FOREST, Keyword.DWELLING)), Keyword.FIERCE, 1));
        return modifiers;
    }
}
