package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.modifiers.OverwhelmedByMultiplierModifier;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.PossessionClass;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Possession • Armor
 * Game Text: Bearer must be a Man. Bearer may not be overwhelmed unless his or her strength is tripled.
 */
public class Card4_278 extends AbstractAttachableFPPossession {
    public Card4_278() {
        super(1, 0, 0, Culture.ROHAN, PossessionClass.ARMOR, "Heavy Chain");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Race.MAN;
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new OverwhelmedByMultiplierModifier(self, Filters.hasAttached(self), 3));
    }
}
