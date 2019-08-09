package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * 4
 * Easterling Beserker
 * Fallen Realms	Minion • Man
 * 2	2	4
 * Easterling. Toil 1.
 * This minion is strength +1 for each wound token you can spot.
 */
public class Card20_110 extends AbstractMinion {
    public Card20_110() {
        super(4, 2, 2, 4, Race.MAN, Culture.FALLEN_REALMS, "Easterling Beserker");
        addKeyword(Keyword.EASTERLING);
        addKeyword(Keyword.TOIL, 1);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        int count = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(game, Filters.character, Filters.wounded)) {
                            count+=game.getGameState().getWounds(physicalCard);
                        }
                        return count;
                    }
                }));
    }
}
