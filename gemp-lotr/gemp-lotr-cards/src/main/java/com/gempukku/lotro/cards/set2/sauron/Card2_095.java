package com.gempukku.lotro.cards.set2.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be a [SAURON] Orc. Bearer is strength +2 while skirmishing a Man or Elf.
 */
public class Card2_095 extends AbstractAttachable {
    public Card2_095() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.SAURON, PossessionClass.HAND_WEAPON, "Vile Blade");
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, Race.ORC);
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self,
                        Filters.and(
                                Filters.hasAttached(self),
                                Filters.inSkirmishAgainst(Filters.or(Race.MAN, Race.ELF))), 2));
        return modifiers;
    }
}
