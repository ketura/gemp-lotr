package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Long-knives of Legolas, Dancing Blades
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +1
 * Card Number: 1R54
 * Game Text: Bearer must be Legolas. Each wounded minion skirmishing Legolas is strength -2.
 */
public class Card40_054 extends AbstractAttachableFPPossession{
    public Card40_054() {
        super(1, 1, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Long-knives of Legolas", "Dancing Blades", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.legolas;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, Filters.and(CardType.MINION, Filters.wounded, Filters.inSkirmishAgainst(Filters.legolas)), -2);
        return Collections.singletonList(modifier);
    }
}
