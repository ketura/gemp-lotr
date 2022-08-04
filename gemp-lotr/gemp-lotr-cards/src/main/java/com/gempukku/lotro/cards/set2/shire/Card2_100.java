package com.gempukku.lotro.cards.set2.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: Mines of Moria
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Stealth. To play, exert 2 Hobbits. Plays to your support area. The twilight cost of each search card and
 * each tracker is +2.
 */
public class Card2_100 extends AbstractPermanent {
    public Card2_100() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, "Fearing the Worst");
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 2, Race.HOBBIT));
    }

        @Override
        public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return List.of(
                new TwilightCostModifier(self, Filters.or(Keyword.SEARCH, Keyword.TRACKER), 2));
    }
}
