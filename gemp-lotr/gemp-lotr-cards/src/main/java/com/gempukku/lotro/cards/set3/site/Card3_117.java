package com.gempukku.lotro.cards.set3.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Twilight Cost: 8
 * Type: Site
 * Site: 8
 * Game Text: River. Maneuver events may not be played.
 */
public class Card3_117 extends AbstractSite {
    public Card3_117() {
        super("Gates of Argonath", 8, 8, Direction.RIGHT);
        addKeyword(Keyword.RIVER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "Maneuver events may not be played", null, new ModifierEffect[]{ModifierEffect.ACTION_MODIFIER}) {
                    @Override
                    public boolean canPlayAction(GameState gameState, ModifiersQuerying modifiersQuerying, Action action, boolean result) {
                        PhysicalCard source = action.getActionSource();
                        if (source != null && source.getBlueprint().getCardType() == CardType.EVENT
                                && modifiersQuerying.hasKeyword(gameState, source, Keyword.MANEUVER))
                            return false;
                        return result;
                    }
                });
    }
}
