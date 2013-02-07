package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.modifiers.PlayersCantUseCardPhaseSpecialAbilitiesModifier;
import com.gempukku.lotro.cards.modifiers.conditions.CanSpotFPCulturesCondition;
import com.gempukku.lotro.cards.modifiers.conditions.NotCondition;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 3
 * Orc Skulker
 * Sauron	Minion • Orc
 * 10	3	6
 * While you cannot spot 3 Free Peoples cultures, characters skirmishing this minion may not use skirmish special abilities.
 */
public class Card20_370 extends AbstractMinion {
    public Card20_370() {
        super(3, 10, 3, 6, Race.ORC, Culture.SAURON, "Orc Skulker");
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new PlayersCantUseCardPhaseSpecialAbilitiesModifier(self,
                new NotCondition(new CanSpotFPCulturesCondition(self.getOwner(), 3)), Phase.SKIRMISH, Filters.character, Filters.inSkirmishAgainst(self));
    }
}
