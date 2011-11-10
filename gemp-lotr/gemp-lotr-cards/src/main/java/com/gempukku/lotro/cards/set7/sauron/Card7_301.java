package com.gempukku.lotro.cards.set7.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 6
 * Game Text: While you can spot 3 other [SAURON] Orcs, this minion is strength +5.
 */
public class Card7_301 extends AbstractMinion {
    public Card7_301() {
        super(4, 11, 3, 6, Race.ORC, Culture.SAURON, "Orc Marauder");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, new SpotCondition(3, Filters.not(self), Culture.SAURON, Race.ORC), 5));
    }
}
