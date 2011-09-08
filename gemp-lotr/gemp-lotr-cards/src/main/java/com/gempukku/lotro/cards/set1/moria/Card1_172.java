package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
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
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 5
 * Type: Minion • Orc
 * Strength: 4
 * Vitality: 3
 * Site: 4
 * Game Text: Archer. While you can spot another [MORIA] Orc, the fellowship archery total is -6.
 */
public class Card1_172 extends AbstractMinion {
    public Card1_172() {
        super(5, 4, 3, 4, Keyword.ORC, Culture.MORIA, "Goblin Archer");
        addKeyword(Keyword.ARCHER);
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot another MORIA Orc, the fellowship archery total is -6.", null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.FREE_PEOPLE
                        && Filters.canSpot(gameState, modifiersLogic, Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.not(Filters.sameCard(self))))
                    return result - 6;
                return result;
            }
        };
    }
}
