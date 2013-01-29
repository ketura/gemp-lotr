package com.gempukku.lotro.cards.set20.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * 0
 * •Faramir's Cloak
 * Gondor	Possession • Cloak
 * Each roaming minion skirmishing a [Gondor] ranger is strength -1.
 */
public class Card20_193 extends AbstractAttachableFPPossession {
    public Card20_193() {
        super(0, 0, 0, Culture.GONDOR, PossessionClass.CLOAK, "Faramir's Cloak", null, true);
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.and(CardType.MINION, Keyword.ROAMING, Filters.inSkirmishAgainst(Culture.GONDOR, Keyword.RANGER)), -1));
    }
}
