package com.gempukku.lotro.cards.set20.wraith;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
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
 * 8
 * •The Witch-king, Revealed in Twilight
 * Ringwraith	Minion • Nazgul
 * 14	4	3
 * Twilight.
 * While The Witch-king is not exhausted, he is fierce. The Witch-king is strength +1 for every other twilight card you can spot.
 */
public class Card20_312 extends AbstractMinion {
    public Card20_312() {
        super(8, 14, 4, 3, Race.NAZGUL, Culture.WRAITH, Names.witchKing, "Revealed in Twilight", true);
        addKeyword(Keyword.TWILIGHT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(self, Filters.not(Filters.exhausted)), Keyword.FIERCE));
        modifiers.add(
                new StrengthModifier(self, self, null,
                        new CountSpottableEvaluator(Filters.not(self), Keyword.TWILIGHT)));
        return modifiers;
    }
}
