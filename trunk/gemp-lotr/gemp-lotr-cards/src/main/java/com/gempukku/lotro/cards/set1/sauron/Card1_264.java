package com.gempukku.lotro.cards.set1.sauron;

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
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Condition
 * Game Text: Plays to your support area. While you can spot a [SAURON] Orc, add 1 to the minion archery total.
 */
public class Card1_264 extends AbstractPermanent {
    public Card1_264() {
        super(Side.SHADOW, 3, CardType.CONDITION, Culture.SAURON, Zone.SHADOW_SUPPORT, "Orc Bowmen");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "While you can spot a SAURON Orc, add 1 to the minion archery total.", null, new ModifierEffect[]{ModifierEffect.ARCHERY_MODIFIER}) {
            @Override
            public int getArcheryTotal(GameState gameState, ModifiersQuerying modifiersLogic, Side side, int result) {
                if (side == Side.SHADOW
                        && Filters.canSpot(gameState, modifiersLogic, Filters.culture(Culture.SAURON), Filters.race(Race.ORC)))
                    return result + 1;
                return result;
            }
        };
    }
}
