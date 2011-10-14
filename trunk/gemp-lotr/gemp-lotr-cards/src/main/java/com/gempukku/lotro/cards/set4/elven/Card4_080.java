package com.gempukku.lotro.cards.set4.elven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.AbstractModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Companion • Elf
 * Strength: 5
 * Vitality: 2
 * Resistance: 6
 * Game Text: To play, spot an Elf. The twilight cost of each ranged weapon played on Ordulus is -1.
 */
public class Card4_080 extends AbstractCompanion {
    public Card4_080() {
        super(1, 5, 2, Culture.ELVEN, Race.ELF, null, "Ordulus", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.race(Race.ELF));
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(
                new AbstractModifier(self, "The twilight cost of each ranged weapon played on Ordulus is -1.", Filters.keyword(Keyword.RANGED_WEAPON), new ModifierEffect[]{ModifierEffect.TWILIGHT_COST_MODIFIER}) {
                    @Override
                    public int getPlayOnTwilightCost(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard, PhysicalCard target, int result) {
                        if (target == self)
                            return result - 1;
                        return result;
                    }
                });
    }
}
