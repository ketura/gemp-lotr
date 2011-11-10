package com.gempukku.lotro.cards.set7.rohan;

import com.gempukku.lotro.cards.AbstractPermanent;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddUntilEndOfTurnModifierEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseAndPutCardFromDiscardIntoHandEffect;
import com.gempukku.lotro.cards.effects.choose.ChooseOpponentEffect;
import com.gempukku.lotro.cards.modifiers.MoveLimitModifier;
import com.gempukku.lotro.common.*;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.GameUtils;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.DiscardCardsFromPlayEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Rohan
 * Twilight Cost: 3
 * Type: Condition • Support Area
 * Game Text: To play, spot 2 [ROHAN] Men. Regroup: Discard cards from hand equal to the number of cards in
 * an opponent's hand to make the move limit +1 for this turn. Each Shadow player may take up to 4 cards into hand from
 * his or her discard pile. Discard this condition.
 */
public class Card7_251 extends AbstractPermanent {
    public Card7_251() {
        super(Side.FREE_PEOPLE, 3, CardType.CONDITION, Culture.ROHAN, Zone.SUPPORT, "Stern People");
    }

    @Override
    public boolean checkPlayRequirements(String playerId, LotroGame game, PhysicalCard self, int twilightModifier, boolean ignoreRoamingPenalty) {
        return super.checkPlayRequirements(playerId, game, self, twilightModifier, ignoreRoamingPenalty)
                && PlayConditions.canSpot(game, 2, Culture.ROHAN, Race.MAN);
    }

    @Override
    protected List<? extends Action> getExtraPhaseActions(final String playerId, final LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && canDiscardCardsEqualToOpponentsHandSize(playerId, game)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseOpponentEffect(playerId) {
                        @Override
                        protected void opponentChosen(String opponentId) {
                            action.insertCost(
                                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, game.getGameState().getHand(opponentId).size()));
                        }
                    });
            action.appendEffect(
                    new AddUntilEndOfTurnModifierEffect(
                            new MoveLimitModifier(self, 1)));
            for (String opponentId : GameUtils.getOpponents(game, playerId)) {
                action.appendEffect(
                        new ChooseAndPutCardFromDiscardIntoHandEffect(action, opponentId, 0, 4, Filters.any));
            }
            action.appendEffect(
                    new DiscardCardsFromPlayEffect(self, self));
            return Collections.singletonList(action);
        }
        return null;
    }

    private boolean canDiscardCardsEqualToOpponentsHandSize(String playerId, LotroGame game) {
        for (String opponentId : GameUtils.getOpponents(game, playerId)) {
            if (PlayConditions.canDiscardFromHand(game, playerId, game.getGameState().getHand(opponentId).size(), Filters.any))
                return true;
        }
        return false;
    }
}
