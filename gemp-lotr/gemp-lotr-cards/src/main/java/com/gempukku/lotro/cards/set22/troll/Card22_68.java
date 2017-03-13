package com.gempukku.lotro.cards.set22.troll;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Short Rest
 * Side: Shadow
 * Culture: Troll
 * Twilight Cost: 1
 * Type: Possession • Hand Weapon
 * Strength: +3
 * Game Text: Bearer must be a Troll. Bearer is damage +1.
 */
public class Card22_68 extends AbstractAttachable {
    public Card22_68() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.TROLL, PossessionClass.HAND_WEAPON, "Troll Knife");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.TROLL;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new StrengthModifier(self, Filters.hasAttached(self), 3));
        modifiers.add(new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
        return modifiers;
    }
}