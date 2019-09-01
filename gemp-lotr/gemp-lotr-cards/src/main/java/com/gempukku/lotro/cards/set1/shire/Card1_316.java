package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.modifiers.AbstractExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.TwilightCostModifier;
import com.gempukku.lotro.logic.modifiers.cost.ExertExtraPlayCostModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: Stealth. To play, exert a Hobbit. Plays to your support area. Each time the fellowship moves, spot 2
 * Hobbit companions to make the shadow number -1 (or spot 4 to make it -2).
 */
public class Card1_316 extends AbstractPermanent {
    public Card1_316() {
        super(Side.FREE_PEOPLE, 0, CardType.CONDITION, Culture.SHIRE, "A Talent for Not Being Seen", null, true);
        addKeyword(Keyword.STEALTH);
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, Race.HOBBIT));
    }

        @Override
        public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new TwilightCostModifier(self, CardType.SITE, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                int hobbitsCount = Filters.countActive(game, CardType.COMPANION, Race.HOBBIT);
                                if (hobbitsCount >= 4)
                                    return -2;
                                if (hobbitsCount >= 2)
                                    return -1;
                                return 0;
                            }
                        })
        );
    }
}
