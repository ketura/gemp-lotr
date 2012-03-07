package com.gempukku.lotro.cards.set18.orc;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Treachery & Deceit
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 2
 * Site: 4
 * Game Text: Hunter 1. (While skirmishing a non-hunter character, this character is strength +1.)
 * This minion is strength +3 for each companion in the dead pile (limit +6).
 */
public class Card18_089 extends AbstractMinion {
    public Card18_089() {
        super(5, 10, 2, 4, Race.ORC, Culture.ORC, "Orkish Headsman");
        addKeyword(Keyword.HUNTER, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        return 3 * Math.min(2, Filters.filter(gameState.getDeadPile(gameState.getCurrentPlayerId()), gameState, modifiersQuerying, CardType.COMPANION).size());
                    }
                });
    }
}
