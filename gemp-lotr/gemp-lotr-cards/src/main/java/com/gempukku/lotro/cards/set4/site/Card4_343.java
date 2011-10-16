package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.LocationCondition;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Twilight Cost: 2
 * Type: Site
 * Site: 4T
 * Game Text: Mountain. The twilight cost of each companion and ally played at Ered Nimrais is +3.
 */
public class Card4_343 extends AbstractSite {
    public Card4_343() {
        super("Ered Nimrais", Block.TWO_TOWERS, 4, 2, Direction.RIGHT);
        addKeyword(Keyword.MOUNTAIN);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.or(CardType.COMPANION, CardType.ALLY), new LocationCondition(Filters.sameCard(self)), 3));
    }
}
