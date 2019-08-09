package com.gempukku.lotro.cards.set19.rohan;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Ages End
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 7
 * Game Text: Valiant. Eomer is strength +2 for each wound on each minion he is skirmishing.
 */
public class Card19_025 extends AbstractCompanion {
    public Card19_025() {
        super(3, 7, 3, 7, Culture.ROHAN, Race.MAN, null, Names.eomer, "Eored Captain", true);
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard self) {
                        int wounds = 0;
                        for (PhysicalCard woundedMinion : Filters.filterActive(game, CardType.MINION, Filters.wounded, Filters.inSkirmishAgainst(self))) {
                            wounds += game.getGameState().getWounds(woundedMinion);
                        }
                        return wounds * 2;
                    }
                });
    }
}
