package com.gempukku.lotro.cards.set11.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 8
 * Vitality: 3
 * Site: 4
 * Game Text: While this minion is at an underground site, each character skirmishing it is strength -2. While you can
 * spot 6 companions, each character skirmishing an [ORC] minion is strength -2.
 */
public class Card11_122 extends AbstractMinion {
    public Card11_122() {
        super(4, 8, 3, 4, Race.ORC, Culture.ORC, "Frenzied Orc");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.inSkirmishAgainst(self), new LocationCondition(Keyword.UNDERGROUND), -2));
        modifiers.add(
                new StrengthModifier(self, Filters.inSkirmishAgainst(Culture.ORC, CardType.MINION), new SpotCondition(6, CardType.COMPANION), -2));
        return modifiers;
    }
}
