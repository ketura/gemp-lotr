package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a Nazgul. While you can spot 3 burdens, bearer is damage +1.
 */
public class Card1_218 extends AbstractAttachable {
    public Card1_218() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.WRAITH, Keyword.HAND_WEAPON, "Nazgul Sword");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.NAZGUL);
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new AbstractModifier(self, "Strength +2, While you can spot 3 burdens, bearer is damage +1", Filters.hasAttached(self), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER, ModifierEffect.KEYWORD_MODIFIER}) {
            @Override
            public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                return result + 2;
            }

            @Override
            public int getKeywordCount(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, int result) {
                if (keyword == Keyword.DAMAGE && gameState.getBurdens() >= 3)
                    return result + 1;
                return result;
            }

            @Override
            public boolean hasKeyword(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, Keyword keyword, boolean result) {
                if (keyword == Keyword.DAMAGE && gameState.getBurdens() >= 3)
                    return true;
                return result;
            }
        };
    }
}
