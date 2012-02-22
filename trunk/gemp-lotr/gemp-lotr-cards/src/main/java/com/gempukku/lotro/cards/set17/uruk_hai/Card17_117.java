package com.gempukku.lotro.cards.set17.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.VitalityModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 0
 * Type: Possession • Hand Weapon
 * Strength: +1
 * Vitality: +1
 * Game Text: Bearer must be an [URUK-HAI] Uruk-hai.
 * Rarity: U
 */
public class Card17_117 extends AbstractAttachable {
    public Card17_117() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.URUK_HAI, PossessionClass.HAND_WEAPON, "Spear of the White Hand");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.URUK_HAI, Race.URUK_HAI);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 1));
        modifiers.add(
                new VitalityModifier(self, Filters.hasAttached(self), 1));
        return modifiers;
    }
}
