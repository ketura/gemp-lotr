package com.gempukku.lotro.cards.set20.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * 2
 * •Glamdring, Turgon's Blade
 * Gandalf	Possession • Hand Weapon
 * 2
 * Bearer must be Gandalf.
 * While skirmishing an Orc, Gandalf is damage +1.
 */
public class Card20_160 extends AbstractAttachableFPPossession {
    public Card20_160() {
        super(2, 2, 0, Culture.GANDALF, PossessionClass.HAND_WEAPON, "Glamdring", "Turgon's Blade", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.gandalf;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Race.ORC)), Keyword.DAMAGE, 1));
    }
}
