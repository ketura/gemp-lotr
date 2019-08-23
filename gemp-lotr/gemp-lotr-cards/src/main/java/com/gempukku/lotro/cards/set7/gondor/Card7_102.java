package com.gempukku.lotro.cards.set7.gondor;

import com.gempukku.lotro.common.*;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.cardtype.AbstractPermanent;
import com.gempukku.lotro.logic.effects.ChooseAndDiscardCardsFromHandEffect;
import com.gempukku.lotro.logic.effects.choose.ChooseAndPlayCardFromDiscardEffect;
import com.gempukku.lotro.logic.timing.PlayConditions;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Return of the King
 * Side: Free
 * Culture: Gondor
 * Twilight Cost: 1
 * Type: Condition • Support Area
 * Game Text: To play, spot 3 [GONDOR] Men. Regroup: While you have only 4 cards in hand, you may discard 2 cards from
 * hand to play a [GONDOR] fortification from your discard pile.
 */
public class Card7_102 extends AbstractPermanent {
    public Card7_102() {
        super(Side.FREE_PEOPLE, 1, CardType.CONDITION, Culture.GONDOR, "Hasty Repairs");
    }

    @Override
    public boolean checkPlayRequirements(LotroGame game, PhysicalCard self) {
        return PlayConditions.canSpot(game, 3, Culture.GONDOR, Race.MAN);
    }

    @Override
    public List<? extends ActivateCardAction> getPhaseActionsInPlay(String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game, Phase.REGROUP, self)
                && game.getGameState().getHand(playerId).size() == 4
                && PlayConditions.canPlayFromDiscard(playerId, game, Culture.GONDOR, Keyword.FORTIFICATION)) {
            ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(
                    new ChooseAndDiscardCardsFromHandEffect(action, playerId, false, 2));
            action.appendEffect(
                    new ChooseAndPlayCardFromDiscardEffect(playerId, game, Culture.GONDOR, Keyword.FORTIFICATION));
            return Collections.singletonList(action);
        }
        return null;
    }
}
