package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.DefaultCostToEffectAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.*;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession • Pipe
 * Game Text: Bearer must be a [GONDOR] companion. Fellowship: Discard a pipeweed possession and spot X pipes to heal
 * X companions.
 */
public class Card1_091 extends AbstractAttachableFPPossession {
    public Card1_091() {
        super(1, Culture.GONDOR, Keyword.PIPE, "Aragorn's Pipe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Filters.culture(Culture.GONDOR), Filters.type(CardType.COMPANION));
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {

        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION))) {
            final DefaultCostToEffectAction action = new DefaultCostToEffectAction(self, Keyword.FELLOWSHIP, "Discard a pipeweed possession and spot X pipes to heal X companions.");
            action.addCost(
                    new ChooseActiveCardEffect(playerId, "Choose pipeweed", Filters.keyword(Keyword.PIPEWEED), Filters.type(CardType.POSSESSION)) {
                        @Override
                        protected void cardSelected(PhysicalCard pipeweed) {
                            action.addCost(new DiscardCardFromPlayEffect(self, pipeweed));
                        }
                    });
            action.addEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose number of pipes new RemoveBurdenEffect(playerId)you wish to spot", game, Filters.keyword(Keyword.PIPE), Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    int companionsCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Filters.type(CardType.COMPANION));
                                    spotCount = Math.min(spotCount, companionsCount);
                                    for (int i = 0; i < spotCount; i++)
                                        action.addEffect(
                                                new ChooseActiveCardsEffect(playerId, "Choose companions", spotCount, spotCount, Filters.type(CardType.COMPANION)) {
                                                    @Override
                                                    protected void cardsSelected(List<PhysicalCard> cards) {
                                                        for (PhysicalCard card : cards)
                                                            action.addEffect(new HealCharacterEffect(playerId, card));
                                                    }
                                                });
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;

    }
}
