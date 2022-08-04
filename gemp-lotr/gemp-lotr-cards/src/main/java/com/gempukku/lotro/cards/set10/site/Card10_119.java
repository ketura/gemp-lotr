package com.gempukku.lotro.cards.set10.site;

import com.gempukku.lotro.common.SitesBlock;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractSite;
import com.gempukku.lotro.logic.modifiers.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: Mount Doom
 * Twilight Cost: 8
 * Type: Site
 * Site: 5K
 * Game Text: Wounds cannot be prevented or healed. Burdens cannot be removed.
 */
public class Card10_119 extends AbstractSite {
    public Card10_119() {
        super("Steward's Tomb", SitesBlock.KING, 5, 8, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<>();
        modifiers.add(
                new CantHealModifier(self, Filters.character));
        modifiers.add(
                new SpecialFlagModifier(self, ModifierFlag.CANT_PREVENT_WOUNDS));
        modifiers.add(
                new CantRemoveBurdensModifier(self, null, Filters.any));
        return modifiers;
    }
}
