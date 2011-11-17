package com.gempukku.lotro.cards.set10.wraith;

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

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 11
 * Vitality: 3
 * Site: 4
 * Game Text: For each wound on the Ring-bearer, each [WRAITH] Orc is strength +1.
 */
public class Card10_059 extends AbstractMinion {
    public Card10_059() {
        super(5, 11, 3, 4, Race.ORC, Culture.WRAITH, "Gorbag", true);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Culture.WRAITH, Race.ORC), null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return gameState.getWounds(gameState.getRingBearer(game.getGameState().getCurrentPlayerId()));
                            }
                        }));
    }
}
