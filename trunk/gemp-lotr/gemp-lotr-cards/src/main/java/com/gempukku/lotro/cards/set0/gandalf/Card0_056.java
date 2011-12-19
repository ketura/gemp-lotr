package com.gempukku.lotro.cards.set0.gandalf;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
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
 * Set: Promotional
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 2
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 5
 * Game Text: While you can spot 2 other Men, Ghan-buri-Ghan cannot be overwhelmed unless his strength is tripled. While
 * at a forest site, Ghan-buri-Ghan is defender +1.
 */
public class Card0_056 extends AbstractCompanion {
    public Card0_056() {
        super(2, 6, 3, 5, Culture.GANDALF, Race.MAN, null, "Ghân-buri-Ghân", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new OverwhelmedByMultiplierModifier(self, self, new SpotCondition(2, Filters.not(self), Race.MAN), 3));
        modifiers.add(
                new KeywordModifier(self, self, new LocationCondition(Keyword.FOREST), Keyword.DEFENDER, 1));
        return modifiers;
    }
}
