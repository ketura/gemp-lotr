package com.gempukku.lotro.cards.set11.men;

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
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: While this minion is at a plains site, it is an archer. While you can spot 6 companions, each [MEN] minion
 * is an archer.
 */
public class Card11_081 extends AbstractMinion {
    public Card11_081() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Fletcher of Harad");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.PLAINS), Keyword.ARCHER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Culture.MEN, CardType.MINION), new SpotCondition(6, CardType.COMPANION), Keyword.ARCHER, 1));
        return modifiers;
    }
}
