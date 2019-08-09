package com.gempukku.lotro.cards.set11.men;

import com.gempukku.lotro.logic.cardtype.AbstractMinion;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.Modifier;
import com.gempukku.lotro.logic.modifiers.StrengthModifier;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Shadows
 * Side: Shadow
 * Culture: Men
 * Twilight Cost: 2
 * Type: Minion • Man
 * Strength: 4
 * Vitality: 1
 * Site: 4
 * Game Text: This minion is strength +1 for each card in the Free Peoples player's hand.
 */
public class Card11_090 extends AbstractMinion {
    public Card11_090() {
        super(2, 4, 1, 4, Race.MAN, Culture.MEN, "Man of Bree");
    }

    @Override
    public java.util.List<? extends Modifier> getAlwaysOnModifiers(LotroGame game, PhysicalCard self) {
        return java.util.Collections.singletonList(new StrengthModifier(self, self, null,
                new Evaluator() {
                    @Override
                    public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                        return game.getGameState().getHand(game.getGameState().getCurrentPlayerId()).size();
                    }
                }));
    }
}
