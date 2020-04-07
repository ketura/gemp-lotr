package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.ArcheryTotalModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
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
        super("Hornburg Causeway", SitesBlock.TWO_TOWERS, 7, 6, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(final LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ArcheryTotalModifier(self, Side.SHADOW, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard self) {
                                return 2 * Math.max(0, Filters.countActive(game, Filters.unboundCompanion) - 3);
                            }
                        }));
    }
}
