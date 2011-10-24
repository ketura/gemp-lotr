package com.gempukku.lotro.cards.set1.dwarven;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Possession � Helm
 * Game Text: Bearer must be Gimli. He takes no more than 1 wound during each skirmish phase. Skirmish: Discard Gimli's
 * Helm to prevent all wounds to him.
 */
public class Card1_015 extends AbstractAttachableFPPossession {
    public Card1_015() {
        super(0, 0, 0, Culture.DWARVEN, PossessionClass.HELM, "Gimli's Helm", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gimli");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Filters.hasAttached(self)));
    }
}
