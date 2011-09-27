package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 6
 * Type: Site
 * Site: 8
 * Game Text: River. For each minion archer at Brown Lands, the minion archery total is +1 (limit +4).
 */
public class Card1_357 extends AbstractSite {
    public Card1_357() {
        super("Brown Lands", 8, 6, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "For each minion archer at Brown Lands, the minion archery total is +1 (limit +4).", null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.SHADOW) {
                    int bonus = Math.min(Filters.countActive(gameState, modifiersLogic, Filters.type(CardType.MINION), Filters.keyword(Keyword.ARCHER)), 4);
                    return bonus + result;
                }
                return result;
            }
        };
    }
}
