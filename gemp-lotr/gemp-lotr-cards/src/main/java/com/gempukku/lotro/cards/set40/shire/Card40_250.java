package com.gempukku.lotro.cards.set40.shire;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractAttachableFPPossession;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.logic.effects.CheckPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.ChooseAndHealCharactersEffect;
import com.gempukku.lotro.logic.effects.IncrementPhaseLimitEffect;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndDiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Title: *Frodo's Pipe
 * Set: Second Edition
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Possession - Pipe
 * Card Number: 1U250
 * Game Text: Bearer must be Frodo.
 * Fellowship: Discard a pipeweed possession and spot X pipes to heal a companion bearing a pipe X times (limit once per phase).
 */
public class Card40_250 extends AbstractAttachableFPPossession {
    public Card40_250() {
        super(1, 0, 0, Culture.SHIRE, PossessionClass.PIPE, "Frodo's Pipe", null, true);
    }

    @Override
    public Filterable getValidTargetFilter(String playerId, LotroGame game, PhysicalCard self) {
        return Filters.frodo;
    }

    @Override
    public List<? extends Action> getPhaseActionsInPlay(final String playerId, LotroGame game, final PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.FELLOWSHIP, self)
                && Filters.canSpot(game, Keyword.PIPEWEED, CardType.POSSESSION)
        && PlayConditions.checkPhaseLimit(game, self, 1)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new IncrementPhaseLimitEffect(self, 1));
            action.appendCost(
                    new ChooseAndDiscardCardsFromPlayEffect(action, playerId, 1, 1, Keyword.PIPEWEED, CardType.POSSESSION));
            action.appendEffect(
                            new PlayoutDecisionEffect(playerId,
                                    new ForEachYouSpotDecision(1, "Choose number of pipes you wish to spot", game, Integer.MAX_VALUE, PossessionClass.PIPE) {
                                        @Override
                                        public void decisionMade(String result) throws DecisionResultInvalidException {
                                            final int spotCount = getValidatedResult(result);
                                            action.appendEffect(
                                                    new ChooseAndHealCharactersEffect(action, playerId, 1, 1, spotCount, CardType.COMPANION, Filters.hasAttached(PossessionClass.PIPE)));
                                        }
                                    }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
