package com.gempukku.lotro.cards.set20.moria;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.modifiers.CantExertWithCardModifier;
import com.gempukku.lotro.logic.modifiers.CantWoundWithCardModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.LinkedList;
import java.util.List;

/**
 * 1
 * •Whip of Many Thongs, Ancient Serpentine Weapon
 * Moria	Artifact • Hand Weapon
 * 1
 * Bearer must be the Balrog. The Balrog may only be wounded or exerted by spells.
 */
public class Card20_283 extends AbstractAttachable {
    public Card20_283() {
        super(Side.SHADOW, CardType.ARTIFACT, 1, Culture.MORIA, PossessionClass.HAND_WEAPON, "Whip of Many Thongs", "Ancient Serpentine Weapon", true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.balrog;
    }

    @Override
    public boolean isExtraPossessionClass(LotroGame game, PhysicalCard self, PhysicalCard attachedTo) {
        return true;
    }

    @Override
    public int getStrength() {
        return 1;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantExertWithCardModifier(self, Filters.balrog, Filters.not(Keyword.SPELL)));
        modifiers.add(
                new CantWoundWithCardModifier(self, Filters.balrog, Filters.not(Keyword.SPELL)));
        return modifiers;
    }
}
