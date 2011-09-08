package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachable;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Bearer must be a ranger. Limit 1 per ranger. Each site's shadow number is -1.
 */
public class Card1_108 extends AbstractAttachable {
    public Card1_108() {
        super(Side.FREE_PEOPLE, CardType.CONDITION, 0, Culture.GONDOR, "No Stranger to the Shadows");
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.keyword(Keyword.RANGER), Filters.not(Filters.hasAttached(Filters.name("No Stranger to the Shadows"))));
    }

    @Override
    public Modifier getAlwaysOnEffect(PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.type(CardType.SITE), -1);
    }
}
