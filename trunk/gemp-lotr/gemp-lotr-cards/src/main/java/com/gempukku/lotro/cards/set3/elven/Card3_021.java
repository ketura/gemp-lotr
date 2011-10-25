package com.gempukku.lotro.cards.set3.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Realms of Elf-lords
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be Legolas. Each Orc or Uruk-hai skirmishing Legolas is strength -2.
 */
public class Card3_021 extends AbstractAttachableFPPossession {
    public Card3_021() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Long-knives of Legolas", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Legolas");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.or(Filters.race(Race.ORC), Filters.race(Race.URUK_HAI)), Filters.inSkirmishAgainst(Filters.hasAttached(self))), -2));
    }
}
