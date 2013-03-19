package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.GameState;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.ModifiersQuerying;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;

/**
 * 1
 * Shadow in the East
 * Event • Maneuver
 * Spot a [Sauron] minion to add a threat. Add an additional threat for each Free Peoples culture less than 4 you can spot.
 * http://lotrtcg.org/coreset/sauron/shadowintheeast(r1).png
 */
public class Card20_374 extends AbstractEvent {
    public Card20_374() {
        super(Side.SHADOW, 1, Culture.SAURON, "Shadow in the East", Phase.MANEUVER);
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int withTwilightRemoved, int twilightModifier, boolean ignoreRoamingPenalty, boolean ignoreCheckingDeadPile) {
        return super.checkPlayRequirements(playerId, game, self, withTwilightRemoved, twilightModifier, ignoreRoamingPenalty, ignoreCheckingDeadPile)
                && PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddThreatsEffect(playerId, self,
                        new Evaluator() {
                            private Evaluator _eval = new CountFPCulturesEvaluator(self.getOwner());

                            @Override
                            public int evaluateExpression(GameState gameState, ModifiersQuerying modifiersQuerying, PhysicalCard cardAffected) {
                                return 1 + Math.max(0, 4 - _eval.evaluateExpression(gameState, modifiersQuerying, cardAffected));
                            }
                        }));
        return action;
    }
}
