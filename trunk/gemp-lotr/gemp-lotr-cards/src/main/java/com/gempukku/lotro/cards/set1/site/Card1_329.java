package com.gempukku.lotro.cards.set1.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

/**
 * Set: The Fellowship of the Ring
 * Twilight Cost: 1
 * Type: Site
 * Site: 2
 * Game Text: Forest. Stealth events may not be played.
 */
public class Card1_329 extends AbstractSite {
    public Card1_329() {
        super("Breeland Forest", Block.FELLOWSHIP, 2, 1, Direction.LEFT);
        addKeyword(Keyword.FOREST);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new AbstractModifier(self, "Stealth events may not be played", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
            @Override
            public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, Action action, boolean result) {
                PhysicalCard actionSourceCard = action.getActionSource();
                if (actionSourceCard != null && actionSourceCard.getBlueprint().getCardType() == CardType.EVENT && modifiersQuerying.hasKeyword(gameState, actionSourceCard, Keyword.STEALTH))
                    return false;
                return result;
            }
        };
    }
}
