package com.gempukku.lotro.cards.set20.isengard;

import com.gempukku.lotro.logic.cardtype.AbstractEvent;
import com.gempukku.lotro.logic.actions.PlayEventAction;
import com.gempukku.lotro.logic.effects.ChoiceEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDeckEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.effects.AddThreatsEffect;
import com.gempukku.lotro.logic.modifiers.evaluator.Evaluator;
import com.gempukku.lotro.logic.timing.Effect;

import java.util.LinkedList;
import java.util.List;

/**
 * 2
 * Seeking My Counsel
 * Isengard	Event • Shadow
 * Play Saruman from your draw deck or discard pile to add X threats, where X is the fellowship’s current region number.
 */
public class Card20_247 extends AbstractEvent {
    public Card20_247() {
        super(Side.SHADOW, 2, Culture.ISENGARD, "Seeking My Counsel", Phase.SHADOW);
    }

    @Override
    public PlayEventAction getPlayCardAction(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        PlayEventAction action = new PlayEventAction(self);
        List<Effect> possibleCosts = new LinkedList<Effect>();
        possibleCosts.add(
                new ChooseAndPlayCardFromDeckEffect(playerId, Filters.saruman) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Saruman from draw deck";
                    }
                });
        possibleCosts.add(
                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Filters.saruman) {
                    @Override
                    public String getText(LotroGame game) {
                        return "Play Saruman from discard pile";
                    }
                });
        action.appendCost(
                new ChoiceEffect(action, playerId, possibleCosts));
        action.appendEffect(
                new AddThreatsEffect(playerId, self,
                        new Evaluator() {
                            @Override
                            public int evaluateExpression(LotroGame game, PhysicalCard cardAffected) {
                                return GameUtils.getRegion(game);
                            }
                        }));
        return action;
    }
}
