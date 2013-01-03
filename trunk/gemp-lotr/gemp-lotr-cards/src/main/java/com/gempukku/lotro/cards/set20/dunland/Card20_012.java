package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.evaluator.CountActiveEvaluator;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * Dunlending Raider
 * Dunland	Minion • Man
 * 7	1	3
 * Dunlending Raider is strength +1 for each other [Dunland] Man you can spot.
 */
public class Card20_012 extends AbstractMinion {
    public Card20_012() {
        super(2, 7, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Raider");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null, new CountActiveEvaluator(Filters.not(self), Culture.DUNLAND, Race.MAN)));
        return modifiers;
    }
}
