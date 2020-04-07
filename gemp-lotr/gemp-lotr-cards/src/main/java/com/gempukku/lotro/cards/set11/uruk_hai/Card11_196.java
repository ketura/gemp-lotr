package com.gempukku.lotro.cards.set11.uruk_hai;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Filterable;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractAttachable;
import com.gempukku.lotro.logic.timing.PlayConditions;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Uruk-hai
 * Twilight Cost: 2
 * Type: Condition
 * Strength: -1
 * Resistance: -3
 * Game Text: To play, spot an [URUK-HAI] minion. Bearer must be an unbound companion. Limit 1 per bearer.
 */
public class Card11_196 extends AbstractAttachable {
    public Card11_196() {
        super(Side.SHADOW, CardType.CONDITION, 2, Culture.URUK_HAI, null, "Our Foes Are Weak");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.URUK_HAI, CardType.MINION);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.unboundCompanion, Filters.not(Filters.hasAttached(Filters.name(getTitle()))));
    }

    @Override
    public int getStrength() {
        return -1;
    }

    @Override
    public int getResistance() {
        return -3;
    }
}
