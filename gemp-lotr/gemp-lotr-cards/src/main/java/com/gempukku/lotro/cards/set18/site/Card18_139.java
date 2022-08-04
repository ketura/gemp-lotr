package com.gempukku.lotro.cards.set18.site;

import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractShadowsSite;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Treachery & Deceit
 * Twilight Cost: 1
 * Type: Site
 * Game Text: Wounds cannot be prevented or healed. Burdens cannot be removed.
 */
public class Card18_139 extends AbstractShadowsSite {
    public Card18_139() {
        super("Steward's Tomb", 1, Direction.RIGHT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new SpecialFlagModifier(self, ModifierFlag.CANT_PREVENT_WOUNDS));
        modifiers.add(
                new CantHealModifier(self, Filters.any));
        modifiers.add(
                new CantRemoveBurdensModifier(self, null, Filters.any));
        return modifiers;
    }
}
