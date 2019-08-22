package com.gempukku.lotro.cards.set32.dwarven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Clouds Burst
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Game Text: Bearer must be a [DWARVEN] character. While skirmishing a mounted minion, bearer is strength +1.
 */
public class Card32_004 extends AbstractAttachableFPPossession {
    public Card32_004() {
        super(1, 2, 0, Culture.DWARVEN, PossessionClass.HAND_WEAPON, "Dwarven Spear");
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.DWARVEN);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(CardType.MINION, Filters.mounted)), 1));
    }
}
