package com.gempukku.lotro.cards.set20.sauron;

import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.RemoveThreatsEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndExertCharactersEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * 3
 * •Grond, Wolf's Head
 * Possession • Support Area
 * Regroup: Remove X threats and exert a [Sauron] minion to discard a non-character Free Peoples card that has
 * a twilight cost of X.
 * http://lotrtcg.org/coreset/sauron/grondwh(r1).png
 */
public class Card20_361 extends AbstractPermanent {
    public Card20_361() {
        super(Side.SHADOW, 3, CardType.POSSESSION, Culture.SAURON, "Grond", "Wolf's Head", true);
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)
                && PlayConditions.canExert(self, game, Culture.SAURON, CardType.MINION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new PlayoutDecisionEffect(playerId,
                            new IntegerAwaitingDecision(1, "Choose X", 0, game.getGameState().getThreats()) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int threats = getValidatedResult(result);
                                    action.insertCost(
                                            new RemoveThreatsEffect(self, threats));
                                    action.appendEffect(
                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.not(Filters.character), Side.FREE_PEOPLE, Filters.printedTwilightCost(threats)));
                                }
                            }));
            action.appendCost(
                    new ChooseAndExertCharactersEffect(action, playerId, 1, 1, Culture.SAURON, CardType.MINION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
