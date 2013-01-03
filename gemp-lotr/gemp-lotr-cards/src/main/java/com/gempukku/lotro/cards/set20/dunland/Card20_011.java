package com.gempukku.lotro.cards.set20.dunland;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.LinkedList;
import java.util.List;

/**
 * 3
 * Dunlending Brute
 * Dunland	Minion • Man
 * 9	1	3
 * Dunlending Brute is strength +1 for each [Dunland] Man stacked on a site.
 */
public class Card20_011 extends AbstractMinion {
    public Card20_011() {
        super(3, 9, 1, 3, Race.MAN, Culture.DUNLAND, "Dunlending Brute");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, final PhysicalCard self) {
        final List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, self, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                int count = 0;
                                for (PhysicalCard site : Filters.filterActive(gameState, modifiersQuerying, Filters.siteControlled(self.getOwner())))
                                    count+=Filters.filter(gameState.getStackedCards(site), gameState, modifiersQuerying, Culture.DUNLAND, Race.MAN).size();

                                return count;
                            }
                        }));
        return modifiers;
    }
}
