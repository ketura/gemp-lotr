package com.gempukku.lotro.cards.set4.site;

import com.gempukku.lotro.cards.AbstractSite;
import com.gempukku.lotro.cards.modifiers.evaluator.CountSpottableEvaluator;
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
 * Twilight Cost: 3
 * Type: Site
 * Site: 4T
 * Game Text: Battleground. The Shadow number of this site is +1 for each mounted companion.
 */
public class Card4_346 extends AbstractSite {
    public Card4_346() {
        super("White Rocks", Block.TWO_TOWERS, 4, 3, Direction.RIGHT);
        addKeyword(Keyword.BATTLEGROUND);
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new TwilightCostModifier(self, Filters.sameCard(self), null, new CountSpottableEvaluator(CardType.COMPANION, Filters.mounted)));
    }
}
