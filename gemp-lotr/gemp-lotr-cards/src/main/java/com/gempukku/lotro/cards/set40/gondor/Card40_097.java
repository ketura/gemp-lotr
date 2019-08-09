package com.gempukku.lotro.cards.set40.gondor;

import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.timing.PlayConditions;
import com.gempukku.lotro.logic.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndAddUntilEOPStrengthBonusEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.common.*;
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
 * Title: *Aragorn's Pipe
 * Set: Second Edition
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Possession - Pipe
 * Card Number: 1U97
 * Game Text: Bearer must be a [GONDOR] Man.
 * Skirmish: Discard a pipeweed possession and spot X pipes to make a companion bearing a pipe strength +X
 * (limit once per phase).
 */
public class Card40_097 extends AbstractAttachableFPPossession {
    public Card40_097() {
        super(1, 0, 0, Culture.GONDOR, PossessionClass.PIPE, "Aragorn's Pipe", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.and(Culture.GONDOR, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && PlayConditions.canDiscardFromPlay(self, game, CardType.POSSESSION, Keyword.PIPEWEED)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, CardType.POSSESSION, Keyword.PIPEWEED));
            action.appendEffect(
                    new CheckPhaseLimitEffect(action, self, 1,
                            new PlayoutDecisionEffect(playerId,
                                    new ForEachYouSpotDecision(1, "Choose number of pipes you wish to spot", game, Integer.MAX_VALUE, PossessionClass.PIPE) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            int spotCount = getValidatedResult(result);
                                            action.appendEffect(
                                                    new ChooseAndAddUntilEOPStrengthBonusEffect(action, self, playerId, spotCount, CardType.COMPANION,
                                                            Filters.hasAttached(PossessionClass.PIPE)));
                                        }
                                    })));
            return Collections.singletonList(action);
        }
        return null;
    }
}
