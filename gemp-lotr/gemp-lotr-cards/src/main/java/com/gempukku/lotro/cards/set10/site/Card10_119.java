package com.gempukku.lotro.cards.set10.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.CantHealModifier;
import com.gempukku.lotro.cards.modifiers.CantRemoveBurdensModifier;
import com.gempukku.lotro.cards.modifiers.SpecialFlagModifier;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.ModifierFlag;

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
        super("Steward's Tomb", Block.KING, 4, 8, Direction.LEFT);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        List<Modifier> modifiers = new LinkedList<Modifier>();
        modifiers.add(
                new CantHealModifier(self, Filters.character));
        modifiers.add(
                new SpecialFlagModifier(self, ModifierFlag.CANT_PREVENT_WOUNDS));
        modifiers.add(
                new CantRemoveBurdensModifier(self, null, Filters.any));
        return modifiers;
    }
}
