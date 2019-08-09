package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * Haradhrim Spear
 * Fallen Realms	Possession • Hand Weapon
 * 2
 * Bearer must be a Southron.
 * While Bearer is skirmishing an unwounded character, he is strength +2.
 */
public class Card20_126 extends AbstractAttachable {
    public Card20_126() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.FALLEN_REALMS, PossessionClass.HAND_WEAPON, "Haradhrim Spear");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.SOUTHRON;
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new StrengthModifier(self, Filters.hasAttached(self), 2));
        modifiers.add(
                new StrengthModifier(self, Filters.and(Filters.hasAttached(self), Filters.inSkirmishAgainst(Filters.character, Filters.unwounded)), 2));
        return modifiers;
    }
}
