package com.gempukku.lotro.cards.set15.rohan;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.CountActiveEvaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Hunters
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 6
 * Vitality: 3
 * Resistance: 6
 * Game Text: Valiant. While you can spot a [ROHAN] Man, this companion’s twilight cost is -1.
 * This companion is strength +1 for each hunter minion you can spot.
 */
public class Card15_138 extends AbstractCompanion {
    public Card15_138() {
        super(3, 6, 3, 6, Culture.ROHAN, Race.MAN, null, "Rohirrim Soldier");
        addKeyword(Keyword.VALIANT);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        if (Filters.canSpot(game, Culture.ROHAN, Race.MAN))
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
return Collections.singletonList(new StrengthModifier(self, self, null, new CountActiveEvaluator(CardType.MINION, Keyword.HUNTER)));
}
}
