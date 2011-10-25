package com.gempukku.lotro.cards.set4.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Possession • Cloak
 * Game Text: Bearer must be Faramir. Each roaming minion skirmishing a Ring-bound Man is strength -1.
 */
public class Card4_119 extends AbstractAttachableFPPossession {
    public Card4_119() {
        super(0, 0, 0, Culture.GONDOR, PossessionClass.CLOAK, "Faramir's Cloak", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Faramir");
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self,
                        Filters.and(
                                Filters.type(CardType.MINION),
                                Filters.keyword(Keyword.ROAMING),
                                Filters.inSkirmishAgainst(Filters.and(Filters.race(Race.MAN), Filters.keyword(Keyword.RING_BOUND)))
                        ), -1));
    }
}
