package com.gempukku.lotro.cards.set20.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * ❶ •Dwarven Vigor [Dwa]
 * Condition • Support Area
 * While there is a [Dwa] card stacked on this condition, each Dwarf is strength +1 for each point of vitality over 3 that he has.
 * http://lotrtcg.org/coreset/dwarven/dwarvenvigor(r3).jpg
 */
public class Card20_048 extends AbstractPermanent {
    public Card20_048() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.DWARVEN, "Dwarven Vigor", null, true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, Race.DWARF,
                new Condition() {
                    @Override
                    public boolean isFullfilled(LotroGame game) {
                        return Filters.filter(game.getGameState().getStackedCards(self), game, Culture.DWARVEN).size() > 0;
                    }
                }, new Evaluator() {
            @Override
            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                return Math.max(0, game.getModifiersQuerying().getVitality(game, cardAffected) - 3);
            }
        }
        ));
    }
}
