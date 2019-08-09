package com.gempukku.lotro.cards.set12.men;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

import java.util.Collections;
import java.util.List;

/**
 * Set: Black Rider
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 3
 * Type: Minion • Man
 * Strength: 9
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +1 for each wound on each companion he is skirmishing (or +2 for each if that
 * companion has resistance 2 or less).
 */
public class Card12_063 extends AbstractMinion {
    public Card12_063() {
        super(3, 9, 2, 4, Race.MAN, Culture.MEN, "Easterling Banner-bearer");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        final PhysicalCard firstActive = Filters.findFirstActive(game, CardType.COMPANION, Filters.wounded, Filters.inSkirmishAgainst(self));
                        if (firstActive != null) {
                            int wounds = game.getGameState().getWounds(firstActive);
                            if (Filters.maxResistance(2).accepts(game, firstActive))
                                return 2 * wounds;
                            else
                                return wounds;
                        }

                        return 0;
                    }
                }));
    }
}
