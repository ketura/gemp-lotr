package com.gempukku.lotro.cards.set4.rohan;

import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Names;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Signet;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.cardtype.AbstractCompanion;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Two Towers
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Companion • Man
 * Strength: 7
 * Vitality: 3
 * Resistance: 6
 * Signet: Theoden
 * Game Text: While you can spot a [ROHAN] Man, Eomer's twilight cost is -1. Eomer is strength +2 for each wound
 * on each minion he is skirmishing.
 */
public class Card4_267 extends AbstractCompanion {
    public Card4_267() {
        super(3, 7, 3, 6, Culture.ROHAN, Race.MAN, Signet.THEODEN, Names.eomer, "Third Marshal of Riddermark", true);
    }

    @Override
    public int getTwilightCostModifier(LotroGame game, PhysicalCard self, PhysicalCard target) {
        if (Filters.canSpot(game, Culture.ROHAN, Race.MAN))
            return -1;
        return 0;
    }

    @Override
    public List<? extends Modifier> getInPlayModifiers(LotroGame game, PhysicalCard self) {
        return Collections.singletonList(
                new StrengthModifier(self, self, null,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard self) {
                                final Collection<PhysicalCard> minions = Filters.filterActive(game, Filters.inSkirmishAgainst(self));
                                int wounds = 0;
                                for (PhysicalCard minion : minions)
                                    wounds += game.getGameState().getWounds(minion);
                                return 2 * wounds;
                            }
                        }));
    }
}
