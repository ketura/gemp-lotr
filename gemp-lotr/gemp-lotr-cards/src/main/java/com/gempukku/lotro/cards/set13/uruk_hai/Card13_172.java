package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 5
 * Type: Minion • Uruk-Hai
 * Strength: 15
 * Vitality: 2
 * Site: 5
 * Game Text: This minion is damage +1 for each character in the dead pile.
 */
public class Card13_172 extends AbstractMinion {
    public Card13_172() {
        super(5, 15, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Uruk Outrider");
    }

    @Override
    public java.util.List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return java.util.Collections.singletonList(new KeywordModifier(self, self, null, Keyword.DAMAGE,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return Filters.filter(game.getGameState().getDeadPile(game.getGameState().getCurrentPlayerId()), game, Filters.character).size();
                    }
                }));
    }
}
