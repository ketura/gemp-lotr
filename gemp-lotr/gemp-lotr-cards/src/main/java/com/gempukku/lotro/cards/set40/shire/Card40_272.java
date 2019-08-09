package com.gempukku.lotro.cards.set40.shire;

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
 * Title: *Sting, Baggins Birthright
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1R272
 * Game Text: Bearer must be Frodo. While skirmishing a minion that has strength 8 or less, Frodo is strength +2.
 */
public class Card40_272 extends AbstractAttachableFPPossession {
    public Card40_272() {
        super(1, 2, 0, Culture.SHIRE, PossessionClass.HAND_WEAPON, "Sting",
                "Baggins Birthright", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        StrengthModifier modifier = new StrengthModifier(self, Filters.and(Filters.hasAttached(self),
                Filters.inSkirmishAgainst(CardType.MINION, Filters.lessStrengthThan(9))), 2);
        return Collections.singletonList(modifier);
    }
}
