package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
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
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 1
 * Site: 4
 * Game Text: While at an underground site, this minion is strength +2. While you can spot another [MORIA] Orc, the
 * fellowship archery total is -1.
 */
public class Card1_184 extends AbstractMinion {
    public Card1_184() {
        super(2, 6, 1, 4, Keyword.ORC, Culture.MORIA, "Goblin Wallcrawler");
    }

    @Override
    public Modifier getAlwaysOnEffect(final PhysicalCard self) {
        return new AbstractModifier(self, "Strength +2",
                Filters.and(
                        Filters.sameCard(self),
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return modifiersQuerying.hasKeyword(gameState, gameState.getCurrentSite(), Keyword.UNDERGROUND);
                            }
                        }), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER, ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + 2;
            }

            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.FREE_PEOPLE
                        && Filters.canSpot(gameState, modifiersLogic, Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ORC), Filters.not(Filters.sameCard(self))))
                    return result - 1;
                return result;
            }
        };
    }
}
