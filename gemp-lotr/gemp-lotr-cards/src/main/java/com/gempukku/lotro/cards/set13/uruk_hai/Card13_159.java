package com.gempukku.lotro.cards.set13.uruk_hai;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Map;

/**
 * Set: Bloodlines
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 4
 * Type: Minion • Uruk-Hai
 * Strength: 9
 * Vitality: 2
 * Site: 5
 * Game Text: Damage +1. This minion is strength +1 for each Free Peoples culture token you can spot.
 */
public class Card13_159 extends AbstractMinion {
    public Card13_159() {
        super(4, 9, 2, 5, Race.URUK_HAI, Culture.URUK_HAI, "Assault Denizen");
        addKeyword(Keyword.DAMAGE, 1);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                        int count = 0;
                        for (PhysicalCard physicalCard : Filters.filterActive(gameState, modifiersQuerying, Side.FREE_PEOPLE, Filters.hasAnyCultureTokens(1))) {
                            for (Map.Entry<Token, Integer> tokens : gameState.getTokens(physicalCard).entrySet()) {
                                if (tokens.getKey().getCulture() != null)
                                    count += tokens.getValue();
                            }
                        }

                        return count;
                    }
                });
    }
}
