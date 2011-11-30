package com.gempukku.lotro.cards.set11.elven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: Shadows
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be an Elf. While bearer is at a forest or river site, each minion he or she is skirmishing
 * is strength -1.
 */
public class Card11_016 extends AbstractAttachableFPPossession {
    public Card11_016() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Blade of Lindon");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(CardType.MINION, Filters.inSkirmishAgainst(Filters.hasAttached(self))),
                        new LocationCondition(Filters.or(Keyword.FOREST, Keyword.RIVER)), -1));
    }
}
