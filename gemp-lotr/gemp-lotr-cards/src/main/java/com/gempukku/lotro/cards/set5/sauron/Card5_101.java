package com.gempukku.lotro.cards.set5.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
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
 * Set: Battle of Helm's Deep
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 0
 * Type: Condition
 * Game Text: To play, exert 2 [SAURON] Orcs. Plays to your support area. The Shadow number of each site is +1 for each
 * wound on a Hobbit (limit +3).
 */
public class Card5_101 extends AbstractPermanent {
    public Card5_101() {
        super(Side.SHADOW, 0, CardType.CONDITION, Culture.SAURON, "I'd Make You Squeak");
    }

    @Override
    public List<? extends AbstractExtraPlayCostModifier> getExtraCostToPlay(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new ExertExtraPlayCostModifier(self, self, null, 2, Culture.SAURON, Race.ORC));
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Arrays.asList(
                new TwilightCostModifier(self, CardType.SITE, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard self) {
                                int wounds = 0;
                                for (PhysicalCard hobbit : Filters.filterActive(game, Race.HOBBIT, Filters.wounded)) {
                                    wounds += game.getGameState().getWounds(hobbit);
                                }

                                return Math.min(3, wounds);
                            }
                        }));
    }
}
