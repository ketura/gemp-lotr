package com.gempukku.lotro.cards.set7.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
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
 * Set: The Return of the King
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 1
 * Site: 4
 * Game Text: While the Ring-bearer is exhausted, this minion is strength +3, fierce and damage +1.
 */
public class Card7_187 extends AbstractMinion {
    public Card7_187() {
        super(4, 10, 1, 4, Race.ORC, Culture.WRAITH, "Morgul Brawler");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotCondition(Keyword.RING_BEARER, Filters.exhausted), 3));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Keyword.RING_BEARER, Filters.exhausted), Keyword.FIERCE, 1));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(Keyword.RING_BEARER, Filters.exhausted), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
