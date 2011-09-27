package com.gempukku.lotro.cards.set1.gandalf;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.costs.ChooseAndDiscardCardsFromPlayCost;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.RemoveBurdenEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gandalf
 * Twilight Cost: 0
 * Type: Possession • Pipe
 * Game Text: Bearer must be Gandalf. Fellowship: Discard a pipeweed possession and spot X pipes to remove X burdens.
 */
public class Card1_074 extends AbstractAttachableFPPossession {
    public Card1_074() {
        super(0, 0, 0, Culture.GANDALF, Keyword.PIPE, "Gandalf's Pipe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.name("Gandalf");
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayCost(action, playerId, 1, 1, Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION)));
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose number of pipes you wish to spot", game, Filters.keyword(Keyword.PIPE), Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    for (int i = 0; i < spotCount; i++)
                                        action.appendEffect(new RemoveBurdenEffect(playerId));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
