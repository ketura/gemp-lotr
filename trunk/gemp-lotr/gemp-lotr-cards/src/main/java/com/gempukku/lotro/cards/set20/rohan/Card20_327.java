package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.cards.AbstractCompanion;
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
 * 2
 * •Gamling, Warrior of the Mark
 * Companion • Man
 * 6	3	5
 * Valiant.
 * While you can spot 3 Valiant men, Gamling is strength +2.
 * While you can spot 3 mounted men, Gamling is damage +1.
 */
public class Card20_327 extends AbstractCompanion {
    public Card20_327() {
        super(2, 6, 3, 5, Culture.ROHAN, Race.MAN, null, "Gamling", "Warrior of the Mark", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, new SpotCondition(3, Race.MAN, Keyword.VALIANT), 2));
        modifiers.add(
                new KeywordModifier(self, self, new SpotCondition(3, Race.MAN, Filters.mounted), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
