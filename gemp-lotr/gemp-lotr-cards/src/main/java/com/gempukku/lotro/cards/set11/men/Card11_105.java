package com.gempukku.lotro.cards.set11.men;

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
 * Culture: Men
 * Twilight Cost: 1
 * Type: Condition
 * Resistance: -2
 * Game Text: To play, spot a [MEN] minion. Bearer must be a companion (except the Ring-bearer).
 */
public class Card11_105 extends AbstractAttachable {
    public Card11_105() {
        super(Side.SHADOW, CardType.CONDITION, 1, Culture.MEN, null, "Wielding the Ring");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.MEN, CardType.MINION);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(CardType.COMPANION, Filters.not(Filters.ringBearer));
    }

    @Override
    public int getResistance() {
        return -2;
    }
}
