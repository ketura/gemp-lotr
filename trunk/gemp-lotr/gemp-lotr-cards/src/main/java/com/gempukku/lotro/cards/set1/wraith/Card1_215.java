package com.gempukku.lotro.cards.set1.wraith;

import com.gempukku.lotro.cards.AbstractEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.PutCardFromDeckIntoHandOrDiscardEffect;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Phase;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;

import java.util.LinkedList;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Wraith
 * Twilight Cost: 1
 * Type: Event
 * Game Text: Search. Shadow: Spot X Nazgul to reveal the top X cards of your draw deck. Take into your hand all
 * [WRAITH] cards revealed and discard the rest.
 */
public class Card1_215 extends AbstractEvent {
    public Card1_215() {
        super(Side.SHADOW, Culture.WRAITH, "The Master's Will", Phase.SHADOW);
        addKeyword(Keyword.SEARCH);
    }

    @Override
    public PlayEventAction getPlayCardAction(final String playerId, final LotroGame game, PhysicalCard self, int twilightModifier) {
        final PlayEventAction action = new PlayEventAction(self);
        action.addEffect(
                new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                        new ForEachYouSpotDecision(1, "Choose how many Nazgul you want to spot", game, Filters.keyword(Keyword.NAZGUL), Integer.MAX_VALUE) {
                            @Override
                            public void decisionMade(String result) throws DecisionResultInvalidException {
                                int spotCount = getValidatedResult(result);
                                List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                                spotCount = Math.min(spotCount, deck.size());
                                List<PhysicalCard> revealedCards = new LinkedList<PhysicalCard>(deck.subList(0, spotCount));
                                for (PhysicalCard revealedCard : revealedCards) {
                                    if (revealedCard.getBlueprint().getCulture() == Culture.WRAITH)
                                        action.addEffect(
                                                new PutCardFromDeckIntoHandOrDiscardEffect(revealedCard));
                                    else
                                        action.addEffect(
                                                new DiscardCardFromDeckEffect(playerId, revealedCard));
                                }
                            }
                        }));
        return action;
    }

    @Override
    public int getTwilightCost() {
        return 1;
    }
}
