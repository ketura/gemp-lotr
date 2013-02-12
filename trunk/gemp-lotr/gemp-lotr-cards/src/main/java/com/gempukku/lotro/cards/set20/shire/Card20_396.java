package com.gempukku.lotro.cards.set20.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.modifiers.conditions.AndCondition;
import com.gempukku.lotro.cards.modifiers.conditions.LocationCondition;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

/**
 * 1
 * •Merry, Halfling Conspirator
 * Shire	Companion • Hobbit
 * 3	4	8
 * While Merry is in region 1 and you can spot 3 other Hobbits in the fellowship, the twilight cost of each Shadow event
 * and Shadow condition is +2.
 */
public class Card20_396 extends AbstractCompanion {
    public Card20_396() {
        super(1, 3, 4, 8, Culture.SHIRE, Race.HOBBIT, null, "Merry", "Halfling Conspirator", true);
    }

    @Override
    public Modifier getAlwaysOnModifier(LotroGame game, PhysicalCard self) {
        return new TwilightCostModifier(self, Filters.and(Side.SHADOW, Filters.or(CardType.EVENT, CardType.CONDITION)),
                new AndCondition(new LocationCondition(Filters.region(1)), new SpotCondition(3, Filters.not(self), CardType.COMPANION, Race.HOBBIT)), 2);
    }
}
