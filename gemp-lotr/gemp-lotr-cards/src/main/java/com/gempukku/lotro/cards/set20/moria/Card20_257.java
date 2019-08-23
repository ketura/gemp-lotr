package com.gempukku.lotro.cards.set20.moria;

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
 * 1
 * •Cave Troll's Hammer
 * Moria	Possession • Hand Weapon
 * 3
 * Bearer must be Cave troll of Moria. It is damage + 1
 */
public class Card20_257 extends AbstractAttachable {
    public Card20_257() {
        super(Side.SHADOW, CardType.POSSESSION, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "Cave Troll's Hammer", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name(Names.caveTroll);
    }

    @Override
    public int getStrength() {
        return 3;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new KeywordModifier(self, Filters.hasAttached(self), Keyword.DAMAGE, 1));
        return modifiers;
    }
}
