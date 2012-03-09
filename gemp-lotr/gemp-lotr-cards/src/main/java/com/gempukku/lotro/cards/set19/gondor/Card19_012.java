package com.gempukku.lotro.cards.set19.gondor;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
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
 * Set: Ages End
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 4
 * Type: Companion • Man
 * Strength: 8
 * Vitality: 4
 * Resistance: 8
 * Game Text: Fellowship. Ranger. While the Ring-bearer is the only other companion you can spot, Aragorn is an archer,
 * defender +1, and can take no more than 1 wound each skirmish.
 */
public class Card19_012 extends AbstractCompanion {
    public Card19_012() {
        super(4, 8, 4, 8, Culture.GONDOR, Race.MAN, null, "Aragorn", true);
        addKeyword(Keyword.FELLOWSHIP);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, self,
                        new NotCondition(new SpotCondition(Filters.not(Filters.ringBearer), Filters.not(self))), Keyword.ARCHER, 1));
        modifiers.add(
                new KeywordModifier(self, self,
                        new NotCondition(new SpotCondition(Filters.not(Filters.ringBearer), Filters.not(self))), Keyword.DEFENDER, 1));
        modifiers.add(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1,
                        new NotCondition(new SpotCondition(Filters.not(Filters.ringBearer), Filters.not(self))), self));
        return modifiers;
    }
}
