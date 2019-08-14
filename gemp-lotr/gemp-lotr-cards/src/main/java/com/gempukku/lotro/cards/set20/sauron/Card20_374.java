package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.CountFPCulturesEvaluator;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.PlayConditions;

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
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, Culture.SAURON, CardType.MINION);
    }

    @Override
    public PlayEventAction getPlayEventCardAction(String playerId, LotroGame game, final PhysicalCard self, int twilightModifier) {
        PlayEventAction action = new PlayEventAction(self);
        action.appendEffect(
                new AddThreatsEffect(playerId, self,
                        new Evaluator() {
                            private Evaluator _eval = new CountFPCulturesEvaluator(self.getOwner());

                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                return 1 + Math.max(0, 4 - _eval.evaluateExpression(game, cardAffected));
                            }
                        }));
        return action;
    }
}
