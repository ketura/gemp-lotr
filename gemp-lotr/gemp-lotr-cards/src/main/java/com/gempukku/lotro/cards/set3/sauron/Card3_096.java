package com.gempukku.lotro.cards.set3.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 10
 * Vitality: 3
 * Site: 6
 * Game Text: For each Free Peoples card borne by a character this minion is skirmishing, that character is strength -1.
 */
public class Card3_096 extends AbstractMinion {
    public Card3_096() {
        super(4, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Pillager");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "For each Free Peoples card borne by a character this minion is skirmishing, that character is strength -1",
                        Filters.inSkirmishAgainst(Filters.sameCard(self)), new ModifierEffect[]{ModifierEffect.STRENGTH_MODIFIER}) {
                    @Override
                    public int getStrength(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, int result) {
                        return result - Filters.filter(gameState.getAttachedCards(physicalCard), gameState, modifiersQuerying, Filters.side(Side.FREE_PEOPLE)).size();
                    }
                });
    }
}
