package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.cards.effects.RemoveTwilightEffect;
import com.gempukku.lotro.common.*;
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
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession • Pipe
 * Game Text: Bearer must be a Hobbit. Fellowship: Discard a pipeweed possession and spot X pipes to remove (X).
 */
public class Card1_292 extends AbstractAttachableFPPossession {
    public Card1_292() {
        super(1, 0, 0, Culture.SHIRE, Keyword.PIPE, "The Gaffer's Pipe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.race(Race.HOBBIT);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION))) {
            final ActivateCardAction action = new ActivateCardAction(self, Keyword.FELLOWSHIP);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION)));
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose number of pipes you wish to spot", game, Filters.keyword(Keyword.PIPE), Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    action.appendEffect(new RemoveTwilightEffect(spotCount));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
