package com.gempukku.lotro.cards.set1.elven;

import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Companion � Elf
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Signet: Aragorn
 * Game Text: Ranger. While skirmishing a Nazgul, Arwen is strength +3.
 */
public class Card1_030 extends AbstractCompanion {
    public Card1_030() {
        super(2, 6, 3, 6, Culture.ELVEN, Race.ELF, Signet.ARAGORN, "Arwen", "Daughter of Elrond", true);
        addKeyword(Keyword.RANGER);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self,
                Filters.and(
                        self,
                        Filters.inSkirmish,
                        new Filter() {
                            @Override
                            public boolean accepts(LotroGame game, PhysicalCard physicalCard) {
                                return Filters.canSpot(game, Race.NAZGUL, Filters.inSkirmish);
                            }
                        }), 3));
    }
}
