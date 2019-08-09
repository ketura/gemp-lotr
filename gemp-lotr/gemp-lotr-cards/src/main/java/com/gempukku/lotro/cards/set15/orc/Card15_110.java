package com.gempukku.lotro.cards.set15.orc;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.DiscardFromHandExtraPlayCostModifier;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.KeywordModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.SpotCondition;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 2
 * Type: Minion • Orc
 * Strength: 12
 * Vitality: 2
 * Site: 4
 * Game Text: To play, discard 3 cards from your hand. While you can spot 2 other [ORC] minions, this minion is fierce.
 */
public class Card15_110 extends AbstractMinion {
    public Card15_110() {
        super(2, 12, 2, 4, Race.ORC, Culture.ORC, "Isengard Marauder");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new DiscardFromHandExtraPlayCostModifier(self, self, 3, null, Filters.any));
    }

        @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new KeywordModifier(self, self, new SpotCondition(2, Filters.not(self), Culture.ORC, CardType.MINION), Keyword.FIERCE, 1));
    }
}
