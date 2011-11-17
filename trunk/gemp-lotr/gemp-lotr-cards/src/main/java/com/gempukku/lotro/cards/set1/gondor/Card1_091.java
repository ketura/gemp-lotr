package com.gempukku.lotro.cards.set1.gondor;

import com.gempukku.lotro.cards.AbstractAttachableFPPossession;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filter;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
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
        super(1, 0, 0, Culture.GONDOR, PossessionClass.PIPE, "Aragorn's Pipe", true);
    }

    @Override
    protected Filter getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, CardType.COMPANION);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {

        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game.getGameState(), game.getModifiersQuerying(), Keyword.PIPEWEED, CardType.POSSESSION)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.PIPEWEED, CardType.POSSESSION));
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose number of pipes you wish to spot", game, PossessionClass.PIPE, Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    int companionsCount = Filters.countSpottable(game.getGameState(), game.getModifiersQuerying(), CardType.COMPANION);
                                    spotCount = Math.min(spotCount, companionsCount);
                                    action.appendEffect(
                                            new ChooseAndHealCharactersEffect(action, playerId, spotCount, spotCount, CardType.COMPANION));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;

    }
}
