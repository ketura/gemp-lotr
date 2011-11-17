package com.gempukku.lotro.cards.set7.shire;

import com.gempukku.lotro.cards.AbstractCompanion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.ReturnCardsToHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Companion • Hobbit
 * Strength: 3
 * Vitality: 4
 * Resistance: 6
 * Signet: Theoden
 * Game Text: Skirmish: If Merry is not assigned to a skirmish, return him to your hand to play up to
 * 2 [ROHAN] possessions from your discard pile.
 */
public class Card7_321 extends AbstractCompanion {
    public Card7_321() {
        super(1, 3, 4, Culture.SHIRE, Race.HOBBIT, Signet.THÉODEN, "Merry", true);
    }

    @Override
    protected List<ActivateCardAction> getExtraInPlayPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.SKIRMISH, self)
                && Filters.notAssignedToSkirmish.accepts(game.getGameState(), game.getModifiersQuerying(), self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ReturnCardsToHandEffect(self, self));
            action.appendEffect(
                    new PlayoutDecisionEffect(playerId,
                            new IntegerAwaitingDecision(1, "How many ROHAN possessions do you wish to play from your discard pile?", 0, 2) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int count = getValidatedResult(result);
                                    for (int i = 0; i < count; i++)
                                        action.appendEffect(
                                                new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.ROHAN, CardType.POSSESSION));

                                }
                            })
            );
            return Collections.singletonList(action);
        }
        return null;
    }
}
