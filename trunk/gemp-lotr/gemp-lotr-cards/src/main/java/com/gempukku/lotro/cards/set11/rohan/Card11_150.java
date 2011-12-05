package com.gempukku.lotro.cards.set11.rohan;

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
 * Set: Shadows
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 1
 * Type: Event • Skirmish
 * Game Text: Make a [ROHAN] Man who has resistance 4 or more strength +2 for each wound on each minion he or she
 * is skirmishing.
 */
public class Card11_150 extends AbstractEvent {
    public Card11_150() {
        super(Side.FREE_PEOPLE, 1, Culture.ROHAN, "Rally Cry", Phase.SKIRMISH);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                int woundCount = 0;
                                for (PhysicalCard minion : Filters.filterActive(gameState, modifiersQuerying, CardType.MINION, Filters.wounded, Filters.inSkirmishAgainst(cardAffected))) {
                                    woundCount += gameState.getWounds(minion);
                                }

                                return 2 * woundCount;
                            }
                        }, Culture.ROHAN, Race.MAN, Filters.minResistance(4)));
        return action;
    }
}
