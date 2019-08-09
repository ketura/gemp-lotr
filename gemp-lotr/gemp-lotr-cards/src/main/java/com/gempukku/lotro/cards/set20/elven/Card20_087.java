package com.gempukku.lotro.cards.set20.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * •Gwemegil, Elf-forged Blade
 * Elven	Possession •   Hand Weapon
 * 2
 * Bearer must be an Elf.
 * If bearer is Arwen, she is damage +1.
 * While skirmishing a Nazgul, bearer is strength +2.
 */
public class Card20_087 extends AbstractAttachableFPPossession {
    public Card20_087() {
        super(2, 2, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Gwemegil", "Elf-forged Blade", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.arwen), Keyword.DAMAGE, 1));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Race.NAZGUL)), 2));
        return modifiers;
    }
}
