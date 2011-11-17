package com.gempukku.lotro.cards.set1.moria;

import com.gempukku.lotro.cards.AbstractResponseOldEvent;
import com.gempukku.lotro.cards.actions.PlayEventAction;
import com.gempukku.lotro.cards.decisions.ForEachYouSpotDecision;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.DiscardCardFromDeckEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Keyword;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.EffectResult;
import com.gempukku.lotro.logic.timing.results.ExertResult;
import com.gempukku.lotro.logic.timing.results.WoundResult;

import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Moria
 * Twilight Cost: 0
 * Type: Event
 * Game Text: Response: If the Ring-bearer exerts or takes a wound, discard a card from the top of your draw deck for
 * each [MORIA] minion you spot. For each Shadow card discarded in this way, add a burden (limit 3 burdens).
 */
public class Card1_189 extends AbstractResponseOldEvent {
    public Card1_189() {
        super(Side.SHADOW, Culture.MORIA, "Lost to the Goblins");
    }

    @Override
    public int getTwilightCost() {
        return 0;
    }

    @Override
    public List<PlayEventAction> getOptionalAfterActions(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (((
                effectResult.getType() == EffectResult.Type.EXERT
                        && Filters.filter(((ExertResult) effectResult).getExertedCards(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).size() > 0)
                || (
                effectResult.getType() == EffectResult.Type.WOUND
                        && Filters.filter(((WoundResult) effectResult).getWoundedCards(), game.getGameState(), game.getModifiersQuerying(), Filters.keyword(Keyword.RING_BEARER)).size() > 0))
                && checkPlayRequirements(playerId, game, self, 0, false)) {
            final PlayEventAction action = new PlayEventAction(self);
            action.appendEffect(
                    new PlayoutDecisionEffect(game.getUserFeedback(), playerId,
                            new ForEachYouSpotDecision(1, "Choose how many MORIA minions you wish to spot", game, Filters.and(Culture.MORIA, CardType.MINION), Integer.MAX_VALUE) {
                                @Override
                                public void decisionMade(String result) throws DecisionResultInvalidException {
                                    int spotCount = getValidatedResult(result);
                                    List<? extends PhysicalCard> deck = game.getGameState().getDeck(playerId);
                                    spotCount = Math.min(spotCount, deck.size());
                                    int shadowCardsCount = 0;
                                    for (PhysicalCard card : deck.subList(0, spotCount)) {
                                        if (card.getBlueprint().getSide() == Side.SHADOW)
                                            shadowCardsCount++;
                                        action.appendEffect(new DiscardCardFromDeckEffect(card));
                                    }
                                    int burdens = Math.min(3, shadowCardsCount);
                                    action.appendEffect(new AddBurdenEffect(self, burdens));
                                }
                            }));
            return Collections.singletonList(action);
        }
        return null;
    }
}
