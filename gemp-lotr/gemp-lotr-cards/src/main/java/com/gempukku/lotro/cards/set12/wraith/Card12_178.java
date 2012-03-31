package com.gempukku.lotro.cards.set12.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Nazgul
 * Strength: 9
 * Vitality: 2
 * Site: 3
 * Game Text: The twilight cost of each mount is -1. While Úlairë Nertëa is mounted, each Nazgul is strength +1 and has
 * muster. (At the start of the regroup phase, you may discard a card from hand to draw a card.)
 */
public class Card12_178 extends AbstractMinion {
    public Card12_178() {
        super(4, 9, 2, 3, Race.NAZGUL, Culture.WRAITH, "Úlairë Nertëa", "Black Horseman", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new TwilightCostModifier(self, PossessionClass.MOUNT, -1));
        modifiers.add(
                new StrengthModifier(self, Race.NAZGUL, new SpotCondition(self, Filters.mounted), 1));
        modifiers.add(
                new KeywordModifier(self, Race.NAZGUL, new SpotCondition(self, Filters.mounted), Keyword.MUSTER, 1));
        return modifiers;
    }
}
