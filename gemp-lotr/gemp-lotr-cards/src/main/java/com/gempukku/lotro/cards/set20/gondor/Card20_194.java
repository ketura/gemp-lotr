package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * •Flaming Brand
 * Gondor	Possession •  Hand Weapon
 * 1
 * Bearer must be a [Gondor] Man.
 * This weapon may be borne in addition to one other hand weapon. Bearer is strength +2 and damage +1 while skirmishing a Nazgul.
 */
public class Card20_194 extends AbstractAttachableFPPossession {
    public Card20_194() {
        super(0, 1, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Flaming Brand", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.and (Filters.hasAttached(self), Filters.inSkirmishAgainst(Race.NAZGUL)), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Race.NAZGUL)), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
