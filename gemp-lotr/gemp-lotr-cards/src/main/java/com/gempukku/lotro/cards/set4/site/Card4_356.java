package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 6
 * Type: Site
 * Site: 7T
 * Game Text: Battleground. The minion archery total is +2 for each unbound companion over 3
 */
public class Card4_356 extends AbstractSite {
    public Card4_356() {
        super("Hornburg Causeway", Block.TWO_TOWERS, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard self) {
                                return 2 * Math.max(0, Filters.countActive(game.getGameState(), modifiersQuerying, Filters.unboundCompanion) - 3);
                            }
                        }));
    }
}
