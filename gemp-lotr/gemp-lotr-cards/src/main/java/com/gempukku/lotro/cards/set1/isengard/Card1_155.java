package com.gempukku.lotro.cards.set1.isengard;

import com.gempukku.lotro.cards.AbstractMinion;
import com.gempukku.lotro.cards.PlayConditions;
import com.gempukku.lotro.cards.effects.AddBurdenEffect;
import com.gempukku.lotro.cards.effects.DiscardTopCardFromDeckEffect;
import com.gempukku.lotro.cards.effects.SelfExertEffect;
import com.gempukku.lotro.common.CardType;
import com.gempukku.lotro.common.Culture;
import com.gempukku.lotro.common.Race;
import com.gempukku.lotro.common.Side;
import com.gempukku.lotro.filters.Filters;
import com.gempukku.lotro.game.PhysicalCard;
import com.gempukku.lotro.game.state.LotroGame;
import com.gempukku.lotro.logic.actions.ActivateCardAction;
import com.gempukku.lotro.logic.decisions.DecisionResultInvalidException;
import com.gempukku.lotro.logic.decisions.IntegerAwaitingDecision;
import com.gempukku.lotro.logic.effects.PlayoutDecisionEffect;
import com.gempukku.lotro.logic.timing.EffectResult;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Set: The Fellowship of the Ring
 * Side: Shadow
 * Culture: Isengard
 * Twilight Cost: 2
 * Type: Minion • Uruk-Hai
 * Strength: 7
 * Vitality: 2
 * Site: 5
 * Game Text: Response: If the Ring-bearer puts on The One Ring, exert this minion. Discard a card from the top of your
 * draw deck for each [ISENGARD] minion you spot. Add a burden for each Shadow card discarded in this way.
 */
public class Card1_155 extends AbstractMinion {
    public Card1_155() {
        super(2, 7, 2, 5, Race.URUK_HAI, Culture.ISENGARD, "Uruk Spy");
    }

    @Override
    public List<? extends ActivateCardAction> getOptionalInPlayAfterActions(final String playerId, final LotroGame game, EffectResult effectResult, final PhysicalCard self) {
        if (effectResult.getType() == EffectResult.Type.PUT_ON_THE_ONE_RING
                && PlayConditions.canExert(self, game, self)) {
            final ActivateCardAction action = new ActivateCardAction(self);
            action.appendCost(new SelfExertEffect(action, self));

            int isengardMinionCount = Filters.countActive(game.getGameState(), game.getModifiersQuerying(), Culture.ISENGARD, CardType.MINION);
            if (isengardMinionCount > 0) {
                action.appendEffect(
                        new PlayoutDecisionEffect(playerId,
                                new IntegerAwaitingDecision(1, "Choose number of minions to spot", 0, isengardMinionCount, isengardMinionCount) {
                                    @Override
                                    public void decisionMade(String result) throws DecisionResultInvalidException {
                                        int spotted = getValidatedResult(result);
                                        if (spotted > 0)
                                            action.appendEffect(
                                                    new DiscardTopCardFromDeckEffect(self, playerId, spotted, false) {
                                                        @Override
                                                        protected void cardsDiscardedCallback(Collection<PhysicalCard> cards) {
                                                            int shadow = Filters.filter(cards, game.getGameState(), game.getModifiersQuerying(), Side.SHADOW).size();
                                                            if (shadow > 0)
                                                                action.appendEffect(
                                                                        new AddBurdenEffect(playerId, self, shadow));
                                                        }
                                                    });
                                    }
                                }));
            }
            return Collections.singletonList(action);
        }
        return null;
    }
}
