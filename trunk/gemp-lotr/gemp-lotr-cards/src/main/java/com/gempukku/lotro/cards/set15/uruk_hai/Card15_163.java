package com.gempukku.lotro.cards.set15.uruk_hai;

import com.gempukku.lotro.cards.AbstractAttachable;
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
 * Set: The Hunters
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 3
 * Type: Possession • Hand Weapon
 * Strength: +2
 * Game Text: Bearer must be an [URUK-HAI] Uruk-hai. Bearer gains hunter 1 (While skirmishing a non-hunter character,
 * this character is strength +1.) If bearer is Lurtz, he is fierce.
 */
public class Card15_163 extends AbstractAttachable {
    public Card15_163() {
        super(Side.SHADOW, CardType.POSSESSION, 3, Culture.URUK_HAI, PossessionClass.HAND_WEAPON, "Lurtz's Sword", "Mighty Longsword", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.URUK_HAI, Race.URUK_HAI);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.HUNTER, 1));
        modifiers.add(
                new KeywordModifier(self, Filters.and(Filters.hasAttached(self), Filters.name("Lurtz")), Keyword.FIERCE));
        return modifiers;
    }
}
