package com.gempukku.lotro.cards.set1.shire;

import com.gempukku.lotro.cards.AbstractAlly;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.cards.effects.RevealTopCardsOfDrawDeckEffect;
import com.gempukku.lotro.common.Block;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.effects.AddTwilightEffect;
import com.gempukku.lotro.logic.timing.Action;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Free
 * Culture: Shire
 * Twilight Cost: 1
 * Type: Ally • Home 1 • Hobbit
 * Strength: 2
 * Vitality: 2
 * Site: 1
 * Game Text: Fellowship: If the twilight pool has fewer than 3 twilight tokens, add (2) to reveal the top 3 cards of
 * your draw deck. Take all [SHIRE] cards revealed into hand and discard the rest.
 */
public class Card1_301 extends AbstractAlly {
    public Card1_301() {
        super(1, Block.FELLOWSHIP, 1, 2, 2, Race.HOBBIT, Culture.SHIRE, "Master Proudfoot", true);
    }

    @Override
    protected List<? extends Action> getExtraInPlayPhaseActions(final String playerId, LotroGame game, PhysicalCard self) {
        if (PlayConditions.canUseFPCardDuringPhase(game.getGameState(), Phase.FELLOWSHIP, self)
                && game.getGameState().getTwilightPool() < 3) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new AddTwilightEffect(self, 2));
            action.appendEffect(
                    new RevealTopCardsOfDrawDeckEffect(playerId, 3) {
                        @Override
                        protected void cardsRevealed(List<PhysicalCard> cards) {
                            for (PhysicalCard card : cards) {
                                if (card.getBlueprint().getCulture() == Culture.SHIRE)
                                    action.appendEffect(new PutCardFromDeckIntoHandOrDiscardEffect(card));
                                else
                                    action.appendEffect(new DiscardCardFromDeckEffect(card));
                            }
                        }
                    });
            return Collections.singletonList(action);
        }
        return null;
    }
}
