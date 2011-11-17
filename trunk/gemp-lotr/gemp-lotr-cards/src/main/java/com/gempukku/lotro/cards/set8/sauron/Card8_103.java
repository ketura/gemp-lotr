package com.gempukku.lotro.cards.set8.sauron;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddTokenEffect;
import com.gempukku.lotro.cards.effects.RemoveTokenEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromHandEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: Siege of Gondor
 * Side: Shadow
 * Culture: Sauron
 * Twilight Cost: 3
 * Type: Possession • Support Area
 * Game Text: Engine. Shadow: Play a [SAURON] minion to add a [SAURON] token here. Regroup: Remove X [SAURON] tokens
 * here to discard a Free Peoples card (except a companion) with a twilight cost of X. Discard a [SAURON] minion
 * or this possession.
 */
public class Card8_103 extends AbstractPermanent {
    public Card8_103() {
        super(Side.SHADOW, 3, CardType.POSSESSION, Culture.SAURON, Zone.SUPPORT, "Grond", true);
        addKeyword(Keyword.ENGINE);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.SHADOW, self, 0)
                && PlayConditions.canPlayFromHand(playerId, game, Culture.SAURON, CardType.MINION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndPlayCardFromHandEffect(playerId, game, Culture.SAURON, CardType.MINION));
            action.appendEffect(
                    new AddTokenEffect(self, self, Token.SAURON));
            return Collections.singletonList(action);
        }
        if (PlayConditions.canUseShadowCardDuringPhase(game, Phase.REGROUP, self, 0)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new PlayoutDecisionEffect(playerId,
                            new IntegerAwaitingDecision(1, "How many SAURON tokens you wish to remove?", 0, game.getGameState().getTokenCount(self, Token.SAURON)) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int tokenCount = getValidatedResult(result);
                                    action.insertCost(
                                            new RemoveTokenEffect(self, self, Token.SAURON, tokenCount));
                                    action.appendEffect(
                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Side.FREE_PEOPLE, Filters.printedTwilightCost(tokenCount), Filters.not(CardType.COMPANION)));
                                    action.appendEffect(
                                            new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.or(self, Filters.and(Culture.SAURON, CardType.MINION))));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
