package com.gempukku.lotro.cards.set10.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession • Armor
 * Game Text: Bearer must be a Hobbit. Each Orc and each Uruk-hai skirmishing bearer is strength -1.
 */
public class Card10_113 extends AbstractAttachableFPPossession {
    public Card10_113() {
        super(1, 0, 0, Culture.SHIRE, PossessionClass.ARMOR, "Orc Armor");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.HOBBIT;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.or(Race.ORC, Race.URUK_HAI), Filters.inSkirmishAgainst(Filters.hasAttached(self))), -1));
    }
}
