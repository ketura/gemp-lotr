package com.gempukku.lotro.cards.set12.dwarven;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * Set: Black Rider
 * Side: Free
 * Culture: Dwarven
 * Twilight Cost: 0
 * Type: Event • Skirmish
 * Game Text: Make a Dwarf strength +2 (or +2 for each possession he bears if he has resistance 4 or more).
 */
public class Card12_013 extends AbstractEvent {
    public Card12_013() {
        super(Side.FREE_PEOPLE, 0, Culture.DWARVEN, "Sharp Defense", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                if (modifiersQuerying.getResistance(gameState, cardAffected) >= 4) {
                                    return 2 * Filters.countActive(gameState, modifiersQuerying, CardType.POSSESSION, Filters.attachedTo(cardAffected));
                                } else {
                                    return 2;
                                }
                            }
                        }, Race.DWARF));
        return action;
    }
}
