package com.gempukku.lotro.cards.set17.orc;

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
 * Set: Rise of Saruman
 * Side: Shadow
 * Culture: Orc
 * Twilight Cost: 4
 * Type: Minion • Orc
 * Strength: 6
 * Vitality: 2
 * Site: 4
 * Game Text: This minion is strength +1 for each [ORC] condition in your discard pile.
 */
public class Card17_081 extends AbstractMinion {
    public Card17_081() {
        super(4, 6, 2, 4, Race.ORC, Culture.ORC, "Orkish Maurader");
    }

    @Override
    public List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, final PhysicalCard self) {
        return Collections.singletonList(new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return Filters.filter(game.getGameState().getDiscard(self.getOwner()), game, Culture.ORC, CardType.CONDITION).size();
                    }
                }));
    }
}
