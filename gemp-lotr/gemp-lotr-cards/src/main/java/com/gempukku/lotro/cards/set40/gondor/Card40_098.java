package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.CantTakeMoreThanXWoundsModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Armor
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Armor
 * Card Number: 1C98
 * Game Text: Bearer must be a Man. Bearer takes no more than one wound in a skirmish.
 */
public class Card40_098 extends AbstractAttachableFPPossession {
    public Card40_098() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.ARMOR, "Armor");
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.MAN;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new CantTakeMoreThanXWoundsModifier(self, Phase.SKIRMISH, 1, Filters.hasAttached(self)));
    }
}
