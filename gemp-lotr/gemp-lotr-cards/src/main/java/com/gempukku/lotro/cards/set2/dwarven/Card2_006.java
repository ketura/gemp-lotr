package com.gempukku.lotro.cards.set2.dwarven;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 2
 * Type: Companion • Dwarf
 * Strength: 5
 * Vitality: 3
 * Resistance: 6
 * Game Text: To play, spot a Dwarf. While skirmishing an Uruk-hai, Fror is strength +3.
 */
public class Card2_006 extends AbstractCompanion {
    public Card2_006() {
        super(2, 5, 3, 6, Culture.DWARVEN, Race.DWARF, null, "Fror", true);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Race.DWARF);
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new StrengthModifier(self,
                Filters.and(
                        Filters.sameCard(self),
                        Filters.inSkirmish,
                        new Filter() {
                            @Override
                            public boolean accepts(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard physicalCard) {
                                return Filters.canSpot(gameState, modifiersQuerying, Race.URUK_HAI, Filters.inSkirmish);
                            }
                        }), 3);
    }
}
