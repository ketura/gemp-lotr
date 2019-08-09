package com.gempukku.lotro.cards.set20.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 3
 * •Eomer, Eored Captain
 * Rohan	Companion • Man
 * 7	3	7
 * Valiant. Eomer is strength +2 for each wound on each minion he is skirmishing.
 */
public class Card20_316 extends AbstractCompanion{
    public Card20_316() {
        super(3, 7, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eomer, "Eored Captain", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, final PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        int count = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(game, CardType.MINION, Filters.inSkirmishAgainst(self))) {
                            count+= game.getGameState().getWounds(physicalCard);
                        }
                        return 2*count;
                    }
                });
    }
}
