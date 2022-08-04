package com.gempukku.lotro.cards.set10.sauron;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Game Text: Bearer must be a [SAURON] Minion. If bearer is an Uruk-hai, it is strength +2.
 */
public class Card10_102 extends AbstractAttachable {
    public Card10_102() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.SAURON, PossessionClass.HAND_WEAPON, "Uruk Axe");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.SAURON, CardType.MINION);
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Race.URUK_HAI), 2));
        return modifiers;
    }
}
