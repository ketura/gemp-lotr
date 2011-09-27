package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.common.*;
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
 * Twilight Cost: 1
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [MORIA] archer, the fellowship archery total is -1.
 */
public class Card1_192 extends AbstractPermanent {
    public Card1_192() {
        super(Side.SHADOW, 1, CardType.CONDITION, Culture.MORIA, Zone.SHADOW_SUPPORT, "Pinned Down");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot a MORIA archer, the fellowship archery total is -1.", null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.FREE_PEOPLE
                        && Filters.canSpot(gameState, modifiersLogic, Filters.culture(Culture.MORIA), Filters.keyword(Keyword.ARCHER)))
                    return result - 1;
                return result;
            }
        };
    }
}
