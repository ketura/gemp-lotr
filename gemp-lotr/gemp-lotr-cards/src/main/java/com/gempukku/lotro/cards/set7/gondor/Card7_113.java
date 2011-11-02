package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession • Armor
 * Strength: +2
 * Vitality: -1
 * Game Text: Bearer must be Pippin. While you can spot 3 [GONDOR] Men, Pippin may take no more than 1 wound in
 * a skirmish.
 */
public class Card7_113 extends AbstractAttachableFPPossession {
    public Card7_113() {
        super(0, 2, -1, Culture.GONDOR, PossessionClass.ARMOR, "Pippin's Armor", true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Pippin");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, new SpotCondition(3, Culture.GONDOR, Race.MAN), Filters.hasAttached(self)));
    }
}
