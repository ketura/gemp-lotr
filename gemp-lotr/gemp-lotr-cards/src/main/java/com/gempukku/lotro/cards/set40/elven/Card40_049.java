package com.gempukku.lotro.cards.set40.elven;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Arrays;
import java.util.List;

/**
 * Title: *Gwemegil, Elf-forged Blade
 * Set: Second Edition
 * Side: Free
 * Culture: Elven
 * Twilight Cost: 2
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1R49
 * Game Text: Bearer must be an Elf.
 * If bearer is Arwen, she is damage +1.
 * While skirmishing a Nazgul, bearer is strength +2.
 */
public class Card40_049 extends AbstractAttachableFPPossession{
    public Card40_049() {
        super(2, 2, 0, Culture.ELVEN, PossessionClass.HAND_WEAPON, "Gwemegil", "Elf-forged Blade", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.ELF;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        KeywordModifier modifier1 = new KeywordModifier(self, Filters.and(Filters.name("Arwen"), Filters.hasAttached(self)), Keyword.DAMAGE, 1);
        StrengthModifier modifier2 = new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Race.NAZGUL)), 2);
        return Arrays.asList(modifier1, modifier2);
    }
}
