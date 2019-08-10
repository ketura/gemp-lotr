package com.gempukku.lotro.cards.set40.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Title: *Cave Troll's Hammer
 * Set: Second Edition
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +3
 * Card Number: 1R158
 * Game Text: Bearer must be Cave Troll of Moria. Cave Troll of Moria is damage +1.
 */
public class Card40_158 extends AbstractAttachable {
    public Card40_158() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "Cave Troll's Hammer", null, true);
    }

    @Override
    public Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Cave Troll of Moria");
    }

    @Override
    public int getStrength() {
        return 3;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE));
        return modifiers;
    }
}
