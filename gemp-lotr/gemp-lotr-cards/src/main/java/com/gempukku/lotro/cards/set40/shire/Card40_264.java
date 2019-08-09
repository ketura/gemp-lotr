package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.Condition;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;

import java.util.Collections;
import java.util.List;

/**
 * Title: A Promise
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition - Support Area
 * Card Number: 1R264
 * Game Text: While you can spot Frodo and Sam and neither is exhausted, each is strength +1.
 */
public class Card40_264 extends AbstractPermanent {
    public Card40_264() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, "A Promise");
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, Filters.or(Filters.frodo, Filters.sam),
                        new Condition() {
                            @Override
                            public boolean isFullfilled(LotroGame game) {
                                return Filters.canSpot(game, Filters.frodo, Filters.not(Filters.exhausted))
                                        && Filters.canSpot(game, Filters.sam, Filters.not(Filters.exhausted));
                            }
                        }, 1));
    }
}
