package com.gempukku.lotro.cards.set20.fallenRealms;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

/**
 * 2
 * Armored Mumak
 * Fallen Realms	Possession • Mount
 * 2	1
 * Bearer must be a Southron.
 * Bearer may not take more than one wound in each skirmish.
 */
public class Card20_107 extends AbstractAttachable {
    public Card20_107() {
        super(Side.SHADOW, CardType.POSSESSION, 2, Culture.FALLEN_REALMS, PossessionClass.MOUNT, "Armored Mumak");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Keyword.SOUTHRON;
    }

    @Override
    public Modifier getAlwaysOnModifier(PhysicalCard self) {
        return new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Filters.hasAttached(self));
    }
}
