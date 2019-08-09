package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * 1
 * •Sting, Baggins Birthright
 * Shire	Possession • Hand Weapon
 * 2
 * Bearer must be Frodo.
 * While skirmishing a minion with strength of 8 or less, Frodo is strength +2.
 */
public class Card20_413 extends AbstractAttachableFPPossession {
    public Card20_413() {
        super(1, 2, 0, Culture.SHIRE, PossessionClass.HAND_WEAPON, "Sting", "Baggins Birthright", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self),
                        Filters.inSkirmishAgainst(CardType.MINION, Filters.lessStrengthThan(9))), 2));
    }
}
