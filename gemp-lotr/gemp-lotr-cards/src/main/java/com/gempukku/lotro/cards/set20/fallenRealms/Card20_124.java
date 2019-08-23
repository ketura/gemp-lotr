package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 0
 * Halberd of Rhun
 * Fallen Realms	Possession • Hand Weapon
 * 2
 * Bearer must be an Easterling.
 * Bearer is Enduring
 */
public class Card20_124 extends AbstractAttachable {
    public Card20_124() {
        super(Side.SHADOW, CardType.POSSESSION, 0, Culture.FALLEN_REALMS, PossessionClass.HAND_WEAPON, "Halberd of Rhun");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.EASTERLING;
    }

    @Override
    public int getStrength() {
        return 2;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.ENDURING));
        return modifiers;
    }
}
