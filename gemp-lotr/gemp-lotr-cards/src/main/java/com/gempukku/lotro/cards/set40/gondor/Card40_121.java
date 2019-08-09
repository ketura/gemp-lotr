package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.modifiers.condition.LocationCondition;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: Sword of the North
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Hand Weapon
 * Strength: +2
 * Card Number: 1C121
 * Game Text: Bearer must be a [GONDOR] Man. While at a site from your adventure deck, bearer is damage +1.
 */
public class Card40_121 extends AbstractAttachableFPPossession {
    public Card40_121() {
        super(1, 2, 0, Culture.GONDOR, PossessionClass.HAND_WEAPON, "Sword of the North");
    }

    @Override
    protected Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Modifier> getNonBasicStatsModifiers(PhysicalCard self) {
        KeywordModifier modifier = new KeywordModifier(self, Filters.hasAttached(self),
                new LocationCondition(Filters.owner(self.getOwner())), Keyword.DAMAGE, 1);
        return Collections.singletonList(modifier);
    }
}
